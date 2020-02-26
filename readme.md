# create notes
- create from 
- enable appengine
   - Firsts steps [gradle page](https://github.com/GoogleCloudPlatform/app-gradle-plugin)
   - Second step and more complete [using gradle and appengine plugin](https://cloud.google.com/appengine/docs/flexible/java/using-gradle)
- test locally: Inspired from:  [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot/)

## integrate firestore
### resources firestore
- [quick start server client library](https://cloud.google.com/firestore/docs/quickstart-servers)
- [better explinations on appengine initialization](https://firebase.google.com/docs/firestore/quickstart)
- [spring basic resource and dependency injection walk throob](https://www.baeldung.com/spring-annotations-resource-inject-autowire)
- [install cli firebase tool](https://firebase.google.com/docs/cli/#install-cli-mac-linux)

### notes firestore
Testing on localhost it is easear to connect to a remote project datastore. friendly eats is a good target.  

To authenticate you have to setup an environment variable "GOOGLE_APPLICATION_CREDENTIALS" ([more info](https://cloud.google.com/docs/authentication/production#auth-cloud-implicit-java))

In my case
```bash
export GOOGLE_APPLICATION_CREDENTIALS="/home/bogdan/workspace/chessout-v2/backend/firebase-chessout-v2.json"
```
e

## datastore
### spring boot datastore tutorial
- [spring boot datastore tutorial](https://codelabs.developers.google.com/codelabs/cloud-spring-datastore/index.html?index=..%2F..index#0)

## useful bash commands
```bash

# how to kill process listening on port
sudo kill -9 `sudo lsof -t -i:8080`

# start firestore emulator on specific port
firebase emulators:start --only firestore

```

## android inspiration
- [kotlin grid view tutorial](https://www.youtube.com/watch?v=sODa2KgfiNo)
- [firebase view model](https://medium.com/@lgvalle/firebase-viewmodels-livedata-cb64c5ee4f95)
- java architecture components tutorial
  - [ part 1](https://www.youtube.com/watch?v=ARpn-1FPNE4)
  - [ part 2](https://www.youtube.com/watch?v=Jwdty9jQN0E)
  - [ part 3](https://www.youtube.com/watch?v=0cg09tlAAQ0)

- google article [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)

