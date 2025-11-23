import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart' as http;

class ApiClient {
  ApiClient({required this.baseUrl});

  final String baseUrl;

  Future<Map<String, dynamic>> login(String username, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/api/auth/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'username': username, 'password': password}),
    );
    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    }
    throw Exception('Login failed: ${response.body}');
  }

  Future<Map<String, dynamic>> updateTarget({
    required String token,
    required int equipmentId,
    required double min,
    required double max,
  }) async {
    final response = await http.post(
      Uri.parse('$baseUrl/api/targets/$equipmentId'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({'targetMin': min, 'targetMax': max}),
    );
    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    }
    throw Exception('Falha ao atualizar alvo de pH: ${response.body}');
  }

  Future<Map<String, dynamic>?> fetchTelemetry({
    required String token,
    required int equipmentId,
  }) async {
    final response = await http.get(
      Uri.parse('$baseUrl/api/equipment/$equipmentId/telemetry/latest'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );
    if (response.statusCode == 204) return null;
    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    }
    throw Exception('Erro ao buscar telemetria: ${response.body}');
  }

  Future<Map<String, dynamic>> requestReport({
    required String token,
    required int equipmentId,
    required DateTime start,
    required DateTime end,
  }) async {
    final response = await http.post(
      Uri.parse('$baseUrl/api/equipment/$equipmentId/report'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({
        'start': start.toUtc().toIso8601String(),
        'end': end.toUtc().toIso8601String(),
      }),
    );
    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    }
    throw Exception('Erro ao solicitar relatório: ${response.body}');
  }

  Stream<String> openSse(String path) async* {
    final client = http.Client();
    final request = http.Request('GET', Uri.parse('$baseUrl$path'));
    final response = await client.send(request);
    await for (final chunk in response.stream.transform(utf8.decoder)) {
      for (final line in chunk.split('\n')) {
        if (line.startsWith('data:')) {
          yield line.substring(5).trim();
        }
      }
    }
  }
}
