import 'dart:convert';

import 'package:area_spike/main.dart';
import 'package:area_spike/ping_client.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';

void main() {
  test('ping parse {"status":"ok"} sur GET /ping', () async {
    final client = PingClient(client: MockClient((req) async {
      expect(req.url.toString(), 'http://10.0.2.2:8080/ping');
      return http.Response(jsonEncode({'status': 'ok'}), 200);
    }));
    final r = await client.ping('http://10.0.2.2:8080');
    expect(r, isA<PingOk>().having((o) => o.status, 'status', 'ok'));
  });

  test('ping renvoie une erreur sur HTTP 500 et sur adresse invalide', () async {
    final client = PingClient(client: MockClient((_) async => http.Response('', 500)));
    expect(await client.ping('http://x:1'), isA<PingError>());
    expect(await client.ping('pas une url'), isA<PingError>());
  });

  testWidgets('écran : saisie de l\'adresse, tap sur Ping, affichage du statut', (tester) async {
    final client = PingClient(client: MockClient((_) async => http.Response('{"status":"ok"}', 200)));
    await tester.pumpWidget(AreaSpikeApp(client: client));
    await tester.enterText(find.byKey(const Key('server-url')), 'http://10.0.2.2:8080');
    await tester.tap(find.byKey(const Key('ping-button')));
    await tester.pumpAndSettle();
    expect(find.text('Statut : ok'), findsOneWidget);
    // Accessibilité : le champ expose un label lisible par TalkBack.
    expect(find.bySemanticsLabel('Adresse du serveur'), findsOneWidget);
  });
}
