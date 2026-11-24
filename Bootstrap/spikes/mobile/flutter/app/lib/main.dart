import 'package:flutter/material.dart';

import 'ping_client.dart';

void main() => runApp(const AreaSpikeApp());

class AreaSpikeApp extends StatelessWidget {
  const AreaSpikeApp({super.key, this.client});
  final PingClient? client;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'AREA spike',
      theme: ThemeData(colorSchemeSeed: Colors.indigo),
      home: PingScreen(client: client ?? PingClient()),
    );
  }
}

class PingScreen extends StatefulWidget {
  const PingScreen({super.key, required this.client});
  final PingClient client;

  @override
  State<PingScreen> createState() => _PingScreenState();
}

class _PingScreenState extends State<PingScreen> {
  final _url = TextEditingController(text: defaultServerUrl);
  String _result = '—';
  bool _loading = false;

  Future<void> _ping() async {
    setState(() => _loading = true);
    final r = await widget.client.ping(_url.text);
    setState(() {
      _loading = false;
      _result = switch (r) {
        PingOk(:final status) => 'Statut : $status',
        PingError(:final message) => 'Erreur : $message',
      };
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('AREA — ping')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextField(
              key: const Key('server-url'),
              controller: _url,
              keyboardType: TextInputType.url,
              decoration: const InputDecoration(labelText: 'Adresse du serveur'),
            ),
            const SizedBox(height: 16),
            FilledButton(
              key: const Key('ping-button'),
              onPressed: _loading ? null : _ping,
              child: Text(_loading ? 'Ping…' : 'Ping'),
            ),
            const SizedBox(height: 16),
            Semantics(
              liveRegion: true,
              child: Text(_result, key: const Key('ping-result'), style: Theme.of(context).textTheme.titleLarge),
            ),
          ],
        ),
      ),
    );
  }
}
