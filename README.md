# ABN AMRO Android assignment

# Setup
For unauthenticated requests, the GitHub rate limit allows for up to 60 requests per hour and only
shows public repo's. In order to increase the limit and show private repo's, you need to setup 
your own Github PAT. Register for one [here](https://github.com/settings/tokens). Pick an appropriate expiration date.

Next copy/rename `apikey.properties.example` to `apikey.properties` and add your key into the 
`GITHUB_PAT` field. When successful, you should now have up to 5000 requests.

Finally, if you want to run the release variant you can create a new keystore 
via `Build` -> `Generate Signed Bundle/APK...` and store your keystore in a safe location. 
Then copy/rename `keystore.properties.example` to `keystore.properties` and fill in the sensitive 
keystore information from the keystore you created earlier.