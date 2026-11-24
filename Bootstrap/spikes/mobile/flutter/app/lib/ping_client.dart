import 'dart:convert';

import 'package:http/http.dart' as http;

/// Adresse par défaut injectée au build : --dart-define=AREA_SERVER_URL=$AREA_SERVER_URL
/// Aucune adresse n'est écrite en dur : sans define, le champ démarre vide.
const String defaultServerUrl = String.fromEnvironment('AREA_SERVER_URL');

sealed class PingResult {}

class PingOk extends PingResult {
  PingOk(this.status);
  final String status;
}

class PingError extends PingResult {
  PingError(this.message);
  final String message;
}

class PingClient {
  PingClient({http.Client? client}) : _client = client ?? http.Client();
  final http.Client _client;

  Future<PingResult> ping(String baseUrl) async {
    final Uri uri;
    try {
      uri = Uri.parse(baseUrl.trim()).resolve('/ping');
      if (!uri.hasScheme || uri.host.isEmpty) return PingError('Adresse invalide');
    } on FormatException {
      return PingError('Adresse invalide');
    }
    try {
      final res = await _client.get(uri).timeout(const Duration(seconds: 5));
      if (res.statusCode != 200) return PingError('HTTP ${res.statusCode}');
      final body = jsonDecode(res.body);
      if (body is Map && body['status'] is String) return PingOk(body['status'] as String);
      return PingError('Réponse inattendue');
    } catch (e) {
      return PingError('Injoignable : ${e.runtimeType}');
    }
  }
}
