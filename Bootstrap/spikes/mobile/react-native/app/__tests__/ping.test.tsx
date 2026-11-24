import { render, screen, userEvent } from '@testing-library/react-native';

import App from '../App';
import { ping } from '../src/pingClient';

const json = (body: unknown, status = 200) =>
  Promise.resolve(new Response(JSON.stringify(body), { status, headers: { 'Content-Type': 'application/json' } }));

test('ping parse {"status":"ok"} sur GET /ping', async () => {
  const fetchMock = jest.fn((_url: string) => json({ status: 'ok' }));
  const r = await ping('http://10.0.2.2:8080', fetchMock as unknown as typeof fetch);
  expect(fetchMock.mock.calls[0][0]).toBe('http://10.0.2.2:8080/ping');
  expect(r).toEqual({ kind: 'ok', status: 'ok' });
});

test('ping renvoie une erreur sur HTTP 500 et sur adresse invalide', async () => {
  const fetch500 = (() => json({}, 500)) as unknown as typeof fetch;
  expect((await ping('http://x:1', fetch500)).kind).toBe('error');
  expect((await ping('pas une url', fetch500)).kind).toBe('error');
});

test("écran : saisie de l'adresse, tap sur Ping, affichage du statut", async () => {
  const user = userEvent.setup();
  await render(<App fetchImpl={(() => json({ status: 'ok' })) as unknown as typeof fetch} />);
  await user.clear(screen.getByLabelText('Adresse du serveur'));
  await user.type(screen.getByLabelText('Adresse du serveur'), 'http://10.0.2.2:8080');
  await user.press(screen.getByRole('button', { name: 'Ping' }));
  expect(await screen.findByText('Statut : ok')).toBeOnTheScreen();
});
