import { StatusBar } from 'expo-status-bar';
import { useState } from 'react';
import { Pressable, StyleSheet, Text, TextInput, View } from 'react-native';

import { defaultServerUrl, ping } from './src/pingClient';

export default function App({ fetchImpl = fetch }: { fetchImpl?: typeof fetch }) {
  const [url, setUrl] = useState(defaultServerUrl);
  const [result, setResult] = useState('—');
  const [loading, setLoading] = useState(false);

  const onPing = async () => {
    setLoading(true);
    const r = await ping(url, fetchImpl);
    setLoading(false);
    setResult(r.kind === 'ok' ? `Statut : ${r.status}` : `Erreur : ${r.message}`);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title} accessibilityRole="header">AREA — ping</Text>
      <Text nativeID="server-url-label" style={styles.label}>Adresse du serveur</Text>
      <TextInput
        testID="server-url"
        accessibilityLabel="Adresse du serveur"
        accessibilityLabelledBy="server-url-label"
        value={url}
        onChangeText={setUrl}
        keyboardType="url"
        autoCapitalize="none"
        style={styles.input}
      />
      <Pressable
        testID="ping-button"
        accessibilityRole="button"
        accessibilityState={{ disabled: loading }}
        disabled={loading}
        onPress={onPing}
        style={styles.button}
      >
        <Text style={styles.buttonText}>{loading ? 'Ping…' : 'Ping'}</Text>
      </Pressable>
      <Text testID="ping-result" accessibilityLiveRegion="polite" style={styles.result}>{result}</Text>
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 16, paddingTop: 64, backgroundColor: '#fff' },
  title: { fontSize: 28, marginBottom: 24 },
  label: { fontSize: 14, color: '#444' },
  input: { fontSize: 18, borderBottomWidth: 1, borderColor: '#666', paddingVertical: 8, marginBottom: 16 },
  button: { backgroundColor: '#3f51b5', borderRadius: 24, padding: 14, alignItems: 'center', minHeight: 48 },
  buttonText: { color: '#fff', fontSize: 16, fontWeight: '600' },
  result: { fontSize: 22, marginTop: 16 },
});
