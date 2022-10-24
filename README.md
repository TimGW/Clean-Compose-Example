# ABN AMRO Android assignment

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

# Considerations
* Set the signingConfig for release to debug in the build.gradle to prevent crash on initialisation.
* Purposely didn't add (feature) modules because of the size of this assignment.
* Added (optional) GitHub PAT for all API calls to increase the rate limit.
* Created separate models for repo list items and the detail page. I think they should be treated
  as separate entities. Even though they display the same data, this could possibly change in the future.
* Only stored the details page in the database due to time constraints.

# Things to improve
* Add a `RemoteMediator` to also store the list of repo's in the DB. Now it's only cached in the VM.
* Provide a more structured setup for mapping between models domain/network/database.
* Use shared elements from the list and detail page for animations.
* Improve sending Throwable to ViewModel to generate a better error message.
* use enums for repo visibility result.
* Use Compose, improve design and accessibility.
* Improve test coverage.