import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

class RelatorioReplyPage extends StatefulWidget {
  final Map<dynamic, dynamic> dadosView;
  const RelatorioReplyPage({super.key, required this.dadosView});

  @override
  State<RelatorioReplyPage> createState() => _RelatorioReplyPageState();
}

class _RelatorioReplyPageState extends State<RelatorioReplyPage> {
  static const double textFontSize = 18;

  @override
  Widget build(BuildContext context) {
    if (kDebugMode) {
      print("Dados para visualização:");
      print(widget.dadosView);
    }
    return Scaffold(
      appBar: AppBar(
        title: const Text("Relatório"),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(30.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                "Médias no período:",
                style: const TextStyle(fontSize: 24),
              ),
              const SizedBox(height: 16),
              _metricRow('pH médio', widget.dadosView['averagePh'] ?? 0),
              _metricRow('Temperatura média',
                  widget.dadosView['averageTemperature'] ?? 0),
              _metricRow('Luminosidade média',
                  widget.dadosView['averageLuminosity'] ?? 0),
              _metricRow('TDS médio', widget.dadosView['averageTds'] ?? 0),
            ],
          ),
        ),
      ),
    );
  }

  Widget _metricRow(String label, dynamic value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(fontSize: textFontSize)),
          Text(value.toString(), style: const TextStyle(fontSize: textFontSize)),
        ],
      ),
    );
  }
}
