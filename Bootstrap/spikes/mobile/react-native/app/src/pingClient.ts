// Adresse par défaut injectée au bundle : EXPO_PUBLIC_AREA_SERVER_URL=... (variable d'environnement).
// Aucune adresse n'est écrite en dur : sans variable, le champ démarre vide.
export const defaultServerUrl = process.env.EXPO_PUBLIC_AREA_SERVER_URL ?? '';

export type PingResult = { kind: 'ok'; status: string } | { kind: 'error'; message: string };

export async function ping(baseUrl: string, fetchImpl: typeof fetch = fetch): Promise<PingResult> {
  let url: URL;
  try {
    url = new URL('/ping', baseUrl.trim());
    if (!/^https?:$/.test(url.protocol)) return { kind: 'error', message: 'Adresse invalide' };
  } catch {
    return { kind: 'error', message: 'Adresse invalide' };
  }
  try {
    const res = await fetchImpl(url.toString(), { signal: AbortSignal.timeout(5000) });
    if (res.status !== 200) return { kind: 'error', message: `HTTP ${res.status}` };
    const body: unknown = await res.json();
    if (body && typeof body === 'object' && typeof (body as { status?: unknown }).status === 'string') {
      return { kind: 'ok', status: (body as { status: string }).status };
    }
    return { kind: 'error', message: 'Réponse inattendue' };
  } catch (e) {
    return { kind: 'error', message: `Injoignable : ${(e as Error).name}` };
  }
}
