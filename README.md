# SubtitleResync
[![Release](https://jitpack.io/v/bachors/SubtitleResync.svg)](https://jitpack.io/#bachors/SubtitleResync)

Subtitle Resync

Gradle
------
```
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}
```
```
dependencies {
    ...
    compile 'com.github.bachors:SubtitleResync:1.0'
}
```

Usage
-----
```java
String srt = "your subtitle";

new SubtitleResync()
	.subtitle(srt) // subtitle String
	.delay(SubtitleResync.ADD) // or SubtitleResync.REMOVE
	.millisecond(1500) // milliseconds
	.resync(new SubtitleResync.Listener() {
        @Override
        public void onResponse(String srt) {
            // output
            ...
        }
    });
```

<a href="https://play.google.com/store/apps/details?id=com.bachors.subtitlestudio"><h1>DEMO</h1></a>
