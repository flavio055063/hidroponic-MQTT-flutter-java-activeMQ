import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hidroponia/relatorio/reply/relatorio_reply.view.dart';
import 'package:hidroponia/shared/app.shared.dart';

class RelatorioRequestPage extends StatefulWidget {
  const RelatorioRequestPage({super.key});

  @override
  State<RelatorioRequestPage> createState() => _RelatorioRequestPageState();
}

class _RelatorioRequestPageState extends State<RelatorioRequestPage> {
  final _formkey = GlobalKey<FormState>();
  String fieldInicialTemp = "";
  String fieldFinalTemp = "";
  bool waitingReply = false;
  static const double inbetweenPadding = 24;

  Future<void> _requestReport() async {
    if (!_formkey.currentState!.validate()) return;
    try {
      setState(() {
        waitingReply = true;
      });
      final start = DateTime.parse(fieldInicialTemp);
      final end = DateTime.parse(fieldFinalTemp);
      final data = await apiClient.requestReport(
        token: sharedUser['token'],
        equipmentId: sharedUser['equipmentId'],
        start: start,
        end: end,
      );
      if (!mounted) return;
      Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (context) => RelatorioReplyPage(dadosView: data),
          ));
    } catch (e) {
      if (kDebugMode) {
        print(e);
      }
      sharedSMS.currentState?.showSnackBar(
        SnackBar(content: Text('Erro ao gerar relatório: $e')),
      );
    } finally {
      if (mounted) {
        setState(() {
          waitingReply = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Solicitar Relatório"),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(48.0),
        child: Form(
          key: _formkey,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              TextFormField(
                onChanged: (value) {
                  fieldInicialTemp = value;
                },
                decoration: const InputDecoration(
                  hintText: "Inicial (ex: 2024-05-18T12:00:00Z)",
                  labelText: "Inicial",
                ),
                keyboardType: TextInputType.datetime,
                validator: (value) {
                  if (value == null || value == "") {
                    return "Insira um valor";
                  }
                  try {
                    DateTime.parse(value);
                  } catch (_) {
                    return 'Formato inválido. Use ISO 8601';
                  }
                  return null;
                },
              ),
              const SizedBox(height: inbetweenPadding),
              TextFormField(
                onChanged: (value) {
                  fieldFinalTemp = value;
                },
                decoration: const InputDecoration(
                  hintText: "Final (ex: 2024-05-18T13:00:00Z)",
                  labelText: "Final",
                ),
                keyboardType: TextInputType.datetime,
                validator: (value) {
                  if (value == null || value == "") {
                    return "Insira um valor";
                  }
                  try {
                    DateTime.parse(value);
                  } catch (_) {
                    return 'Formato inválido. Use ISO 8601';
                  }
                  return null;
                },
              ),
              const SizedBox(height: inbetweenPadding + 16),
              waitingReply == false
                  ? ElevatedButton(
                      onPressed: _requestReport,
                      child: const Text("Enviar"),
                    )
                  : const CircularProgressIndicator(),
            ],
          ),
        ),
      ),
    );
  }
}
