# spotimap

Something cool

[![Build Status](https://travis-ci.org/kubukoz/spotimap.svg?branch=master)](https://travis-ci.org/kubukoz/spotimap)

# Run in development

1. Create a Spotify Web API app
1. Register `http://localhost:2137/auth/config` as a redirect URL (2137 is the default port, configurable at `server.port`) 
1. Supply your Spotify API tokens to the config, e.g. by creating a file at `src/main/resources/secret.conf`:
```hocon
spotify {
  client-id = "391e962681972d5625fc3574e112a4bd"
  client-secret = "762026d5aadaac8d949628a00b63a3e8"
}
```

1. `sbt app/run`

1. Get an access token, e.g. by clicking the link that will appear in the standard output.

1. Make requests to the application with a `SPOTIFY-TOKEN` header containing the access token.
