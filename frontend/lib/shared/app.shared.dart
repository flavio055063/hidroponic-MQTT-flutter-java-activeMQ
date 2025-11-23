import 'package:flutter/material.dart';
import 'package:hidroponia/shared/api_client.dart';

final ApiClient apiClient = ApiClient(baseUrl: const String.fromEnvironment(
  'API_BASE_URL',
  defaultValue: 'http://localhost:8080',
));

final GlobalKey<ScaffoldMessengerState> sharedSMS =
    GlobalKey<ScaffoldMessengerState>();

Map<String, dynamic> sharedUser = {
  'token': '',
  'equipmentId': null,
  'username': '',
  'admin': false,
};
