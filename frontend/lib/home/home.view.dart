import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hidroponia/shared/app.shared.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  Map<String, dynamic> valoresStream = {};
  Timer? telemetryTimer;
  StreamSubscription<String>? alertSubscription;
  StreamSubscription<String>? targetSubscription;

  @override
  void initState() {
    super.initState();
    _startTelemetryUpdates();
    _subscribeToAlerts();
    _subscribeToTargets();
  }

  @override
  void dispose() {
    telemetryTimer?.cancel();
    alertSubscription?.cancel();
    targetSubscription?.cancel();
    super.dispose();
  }

  void _startTelemetryUpdates() {
    telemetryTimer?.cancel();
    telemetryTimer = Timer.periodic(const Duration(seconds: 5), (_) async {
      await _loadTelemetry();
    });
    _loadTelemetry();
  }

  Future<void> _loadTelemetry() async {
    if (sharedUser['token'] == '' || sharedUser['equipmentId'] == null) return;
    try {
      final response = await apiClient.fetchTelemetry(
        token: sharedUser['token'],
        equipmentId: sharedUser['equipmentId'],
      );
      if (response != null) {
        setState(() {
          valoresStream = response;
        });
      }
    } catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }

  void _subscribeToAlerts() {
    alertSubscription = apiClient.openSse('/api/alerts/stream').listen((event) {
      WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
        showDialog(
          context: context,
          builder: (context) => AlertDialog(
            title: const Text('Alerta'),
            content: Text(event),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(context),
                child: const Text('Ok'),
              ),
            ],
          ),
        );
      });
    });
  }

  void _subscribeToTargets() {
    targetSubscription = apiClient.openSse('/api/targets/stream').listen((event) {
      try {
        final parts = event.split(',');
        if (parts.length == 2) {
          setState(() {
            valoresStream['targetMin'] = double.tryParse(parts[0]);
            valoresStream['targetMax'] = double.tryParse(parts[1]);
          });
        }
      } catch (_) {}
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Hidroponia"),
        actions: [
          IconButton(
            onPressed: () {
              Navigator.pushReplacementNamed(context, '/login');
            },
            icon: const Icon(Icons.exit_to_app_rounded, size: 32),
          )
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(horizontal: 30.0),
        child: Wrap(
          alignment: WrapAlignment.center,
          spacing: 48,
          runSpacing: 48,
          children: [
            Hero(
              tag: 'mainLogo',
              child: Padding(
                padding: const EdgeInsets.only(top: 24.0),
                child: SizedBox.square(
                  dimension: 200,
                  child: Image.asset(
                    'lib/assets/logoCutTransparent.png',
                    fit: BoxFit.contain,
                    semanticLabel: "Logo Hidroponia",
                  ),
                ),
              ),
            ),
            Table(
              defaultVerticalAlignment: TableCellVerticalAlignment.middle,
              children: [
                const TableRow(
                  children: [
                    SizedBox(),
                    Text(
                      "Atual",
                      textAlign: TextAlign.center,
                    ),
                    Text(
                      "Objetivo",
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
                TableRow(
                  children: [
                    ...dataReceivedWidgets(context,
                        titulo: "pH 💧",
                        valorAtual: (valoresStream['ph'] ?? '').toString(),
                        valorObjetivo:
                            "${valoresStream['targetMin'] ?? ''} ~ ${valoresStream['targetMax'] ?? ''}"),
                  ],
                ),
                TableRow(
                  children: [
                    ...dataReceivedWidgets(context,
                        titulo: "Temp 🌡",
                        valorAtual: (valoresStream['temperature'] ?? '').toString(),
                        valorObjetivo: "-"),
                  ],
                ),
                TableRow(
                  children: [
                    ...dataReceivedWidgets(context,
                        titulo: "Lumin 💡",
                        valorAtual: (valoresStream['luminosity'] ?? '').toString(),
                        valorObjetivo: "-"),
                  ],
                ),
                TableRow(
                  children: [
                    ...dataReceivedWidgets(context,
                        titulo: "TDS 🌊",
                        valorAtual: (valoresStream['tds'] ?? '').toString(),
                        valorObjetivo: "-"),
                  ],
                ),
              ],
            ),
            Column(
              children: [
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.pushNamed(context, '/targets');
                    },
                    style: ButtonStyle(
                      backgroundColor: MaterialStatePropertyAll(
                          Theme.of(context).colorScheme.primary),
                    ),
                    child: Text(
                      "Alterar dados Objetivos",
                      style: TextStyle(
                          color: Theme.of(context).colorScheme.onPrimary),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.pushNamed(context, '/relatorio/request');
                    },
                    style: ButtonStyle(
                      backgroundColor: MaterialStatePropertyAll(
                          Theme.of(context).colorScheme.primary),
                    ),
                    child: Text(
                      "Solicitar Relatório",
                      style: TextStyle(
                        color: Theme.of(context).colorScheme.onPrimary,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

List<Widget> dataReceivedWidgets(
  BuildContext context, {
  required String titulo,
  required String valorAtual,
  required String valorObjetivo,
}) {
  const double textSize = 18;
  return [
    Text(
      titulo,
      style: const TextStyle(fontSize: textSize),
      textAlign: TextAlign.end,
    ),
    Container(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
        margin: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
        decoration: BoxDecoration(
          borderRadius: const BorderRadius.all(Radius.circular(8)),
          color: Theme.of(context).colorScheme.secondary,
        ),
        //
        child: Text(
          valorAtual,
          style: TextStyle(
            color: Theme.of(context).colorScheme.onSecondary,
            fontSize: textSize,
          ),
          textAlign: TextAlign.center,
        )),
    //
    Text(
      valorObjetivo,
      style: const TextStyle(
        fontSize: textSize,
      ),
      textAlign: TextAlign.center,
    ),
  ];
}
