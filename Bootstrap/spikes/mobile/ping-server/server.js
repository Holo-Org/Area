// Serveur /ping minimal, utilisé comme cible commune pour les trois spikes mobiles.
// Le port vient de l'environnement (PORT), conformément à la cible du bootstrap.
const http = require("http");
const port = Number(process.env.PORT ?? 8080);

http
  .createServer((req, res) => {
    console.log(`${new Date().toISOString()} ${req.method} ${req.url} ua=${req.headers["user-agent"]}`);
    if (req.method === "GET" && req.url === "/ping") {
      res.writeHead(200, { "Content-Type": "application/json" });
      res.end(JSON.stringify({ status: "ok" }));
      return;
    }
    res.writeHead(404, { "Content-Type": "application/json" });
    res.end(JSON.stringify({ error: "not_found" }));
  })
  .listen(port, "0.0.0.0", () => console.log(`ping-server listening on ${port}`));
