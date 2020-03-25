# Accedo One SDK for Android

```
   _                    _           ___
  /_\   ___ ___ ___  __| | ___     /___\_ __   ___
 //_\\ / __/ __/ _ \/ _` |/ _ \   //  // '_ \ / _ \
/  _  \ (_| (_|  __/ (_| | (_) | / \_//| | | |  __/
\_/ \_/\___\___\___|\__,_|\___/  \___/ |_| |_|\___|

```

[ ![Download](https://api.bintray.com/packages/tibor-pasztor-accedo/accedo-products/accedo-one-sdk-android/images/download.svg) ](https://bintray.com/tibor-pasztor-accedo/accedo-products/accedo-one-sdk-android/_latestVersion)

## Summary

This is the official [Accedo One](https://www.accedo.tv/one) SDK for Android 4.0 and up, previously known as the VDK AppGrid component.
While Accedo One exposes a set of friendly REST APIs, this SDK is intended to provide a smoother experience when coding in Android, providing seamless sync/async services, with automatic caching, and offline mode.

We follow [semantic versioning](http://semver.org/).
Check the [change log](./CHANGELOG.md) for a listing of changes and new features per version.

## Features

The default factory exposed by this SDK allows creating a client instance tied to a device id and an application key. It provides the following features :
 - Easy access to Accedo One APIs.
 - Automatic session creation and re-creation.
 - Automatic disk-caching for Control related data.
 - Disk-cache auto-verified with if-modified-since calls whenever needed.
 - Automatic and seamless fallback to cache versions of Control calls, whenever there's a service outage. 
 - Verbose logcat logging on what's going on at all times.
 - Automatic pagination support for Publish calls.

## Documentation

For the time being, please refer to the JavaDoc inside the definition file of all Services, that you can find here: [API docs](https://github.com/Accedo-Products/accedo-one-sdk-android/tree/master/one-sdk/src/main/java/tv/accedo/one/sdk/definition)

An exported and more detailed version coming soon!

You may also want to refer to the [Accedo One Rest API documentation](https://developer.one.accedo.tv/) that this SDK uses behind the scenes.

## Usage

- Include the library into your buildscript: 

```
repositories {
    jcenter()
}
dependencies {
    implementation 'tv.accedo.one:one-sdk:<latest>'
}
```

- Create a singleton instance of AccedoOne in your service holder or application such as:

```
static final AccedoOne accedoOne = new AccedoOneImpl("appKey", getDeviceId());
```

- Use this instance to access AccedoOne anywhere!

## Examples

Below are a few examples on how to access certain parts of Accedo One via this SDK.

### Using either sync or async accessors

The SDK provides you with both sync and async calls for almost every API it exposes. So if you want to just simply access some metadata, you can simply use the async ones, however if you'd like to embed some Accedo One calls into a larger initialisation process, you may find the sync ones to be more straightforward to use.

Sync example:

```
try {
    String result = accedoOne.control().getMetadata(context, "base_url");
} catch (AccedoOneException e) {
    e.printStackTrace();
}
```

Async example:

```
Cancellable cancellable = accedoOne.control().async().getMetadata(context, "base_url", new Callback<String>() {
    @Override
    public void execute(String result) {

    }
  }, new Callback<AccedoOneException>() {
    @Override
    public void execute(AccedoOneException result) {
                
    }
  });
```

Please note the cancellable returned by async calls. Those can be useful if you'd like to cancel your calls on certain lifecycle events like onPause().

### Publish example

```
try {
    JSONObject result = accedoOne.publish().getEntry(context, "hashId");
} catch (AccedoOneException e) {
    e.printStackTrace();
}
```

## More information & Links

* [Accedo One homepage](https://www.accedo.tv/one)
* [Accedo One help center](https://support.one.accedo.tv)
* [Accedo One API documentation](https://developer.one.accedo.tv)

## Unit Tests

AndroidJUnitTests have been written to cover all of the exported APIs from this module. Async calls are auto-wrapped from sync ones, thus they are not tested separately.

## License

See the [LICENSE file](./LICENSE.md) for license rights and limitations (Apache 2.0)
