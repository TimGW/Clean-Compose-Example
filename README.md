# Clean Compose Example
Android application with clean architecture principles for demo purposes. This app has an offline 
first approach with the database as a single source of truth. Applied the following;
- Patterns: Clean Architecture, Repository, Use Cases, MVVM, Single Activity
- Libraries: Room, Hilt, Kotlin Flow, Moshi, Paging3, Jetpack Compose, Retrofit

# Setup
For unauthenticated requests, the GitHub rate limit allows for up to 60 requests per hour. 
In order to increase this limit, you need to setup your own Github PAT. 
Register for one [here](https://github.com/settings/tokens). Pick an appropriate expiration date.

Next copy/rename `apikey.properties.example` to `apikey.properties` and add your key into the
`GITHUB_PAT` field. When successful, you should now have up to 5000 requests.

Finally, if you want to build a release variant, create a new keystore via `Build` -> 
`Generate Signed Bundle/APK...` and store your keystore in a safe location. 
Copy/rename `keystore.properties.example` to `keystore.properties` and fill in the sensitive
keystore information from the keystore you created earlier. Update the signingConfigs for the 
release variant from `signingConfig signingConfigs.debug` to`signingConfig signingConfigs.release`

# Things to improve
* Provide a more structured setup for mapping between models domain/network/database.
* Improve test coverage.