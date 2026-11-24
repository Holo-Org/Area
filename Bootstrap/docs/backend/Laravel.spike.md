# Laravel Spike
In 15 minutes not every member of the group could be able to create a simple function server using Laravel, because of the setup.
This server should expose a simple GET route that returns a "status" : "ok".

Laravel is a bit complicated to setup: you need to install PHP, Composer and the Laravel installer (and Node.js if we want the frontend assets). Then the API routes are not enabled by default, you have to run `php artisan install:api`, after that a route is a single line in `routes/api.php` (ex: `Route::get('/status', fn () => ['status' => 'ok']);`). For a bigger server we need to declare controllers (classes), which is a different way of thinking compared to Next.js.

Learning cost is high for a developper who is not familiar with PHP and Laravel: the framework is full of conventions (Eloquent, Artisan, service container, middlewares) that need to be learned before being productive.

Laravel is great for AREA because it comes with batteries included: queues, a task scheduler, events, an ORM with migrations, validation... which fit well the workflows / reactions part. Caution: the queue workers and the scheduler are separate processes (`php artisan queue:work`), so we need to run them next to the web server.
Libraries are available for the auth part: Sanctum / Passport for API tokens and OAuth 2.0 server, and Socialite for the OAuth login with external services (Google, GitHub...).

Laravel has a lot of starter kits integrated (React, Vue, Livewire) that contain a dashboard, authentication and other features, so a frontend can live in the same codebase (with Inertia). Otherwise it can be used as a pure API with a separate web client.

Note : For the infrastructure we need to build our own docker image: PHP-FPM + a web server like Nginx (or FrankenPHP / Laravel Octane in a single container), plus one more container (or process) for the queue worker and the scheduler.
