import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hidroponia/shared/app.shared.dart';

class TargetsPage extends StatefulWidget {
  const TargetsPage({super.key});

  @override
  State<TargetsPage> createState() => _TargetsPageState();
}

class _TargetsPageState extends State<TargetsPage> {
  final _formkey = GlobalKey<FormState>();

  double? phTargetMin;
  double? phTargetMax;
  static const double limitephMinimo = 0;
  static const double limitephMaximo = 14;
  static const double inbetweenPadding = 24;

  Future<void> _submitTargets() async {
    if (!_formkey.currentState!.validate()) {
      return;
    }
    if (phTargetMin == null || phTargetMax == null) {
      return;
    }
    try {
      await apiClient.updateTarget(
        token: sharedUser['token'],
        equipmentId: sharedUser['equipmentId'],
        min: phTargetMin!,
        max: phTargetMax!,
      );
      sharedSMS.currentState?.showSnackBar(
        const SnackBar(content: Text('Alvo de pH atualizado.')),
      );
    } catch (e) {
      if (kDebugMode) {
        print(e);
      }
      sharedSMS.currentState?.showSnackBar(
        SnackBar(content: Text('Falha ao atualizar alvo: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Alterar objetivos"),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(48),
        child: Form(
          key: _formkey,
          child: Column(
            children: [
              TextFormField(
                decoration: const InputDecoration(
                  labelText: "pH mínimo",
                ),
                validator: (value) {
                  if (value != null) {
                    if (value != "") {
                      phTargetMin = double.tryParse(value);
                      if (phTargetMin == null) {
                        return "Formato inválido";
                      }
                      if (phTargetMin! < limitephMinimo) {
                        phTargetMin = null;
                        return "Valor muito pequeno";
                      }
                      if (phTargetMin! > limitephMaximo) {
                        phTargetMin = null;
                        return "Valor muito grande";
                      }
                    } else {
                      phTargetMin = null;
                    }
                  }
                  return null;
                },
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: inbetweenPadding),
              TextFormField(
                decoration: const InputDecoration(
                  labelText: "pH máximo",
                ),
                validator: (value) {
                  if (value != null) {
                    if (value != "") {
                      phTargetMax = double.tryParse(value);
                      if (phTargetMax == null) {
                        return "Formato inválido";
                      }
                      if (phTargetMax! > limitephMaximo) {
                        phTargetMax = null;
                        return "Valor muito grande";
                      }
                      if (phTargetMin != null && phTargetMax! < phTargetMin!) {
                        return "Máximo deve ser maior que mínimo";
                      }
                    } else {
                      phTargetMax = null;
                    }
                  }
                  return null;
                },
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: inbetweenPadding),
              ElevatedButton(
                onPressed: _submitTargets,
                child: const Text("Enviar"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
