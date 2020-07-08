# Setup the development environment
Lipor is an eclipse rich client platform app. So you need the eclipse ide to hack it.
Lipor uses the jaxb java library. Since java 11 this library is not part of the jdk any more. Because of that we use the old openjdk 10.

# Exporting the app
Exporting the app, which means making the app startable outside of the ide only worked for me this way: Use java 8 for eclipse and then switch to java 10 for the project in the eclipse preferences.