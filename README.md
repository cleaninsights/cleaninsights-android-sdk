Clean Insights Android SDK
========================

This document describes how to get started using the Clean Insights SDK for Android. 
[CleanInsights](https://cleaninsights.github.io) gives developers a safe, sustainable, and secure way to gather insights about their users using cutting edge techniques like differential privacy, onion routing, certificate pinning and more.

This SDK is based on the Piwik.org Android SDK, and is fully compatible with a Piwik backend server instance. We recommend hosting your own instance of Piwik, securing it with TLS (letsencrypt!), and setting up a Tor Onion Service (".onion") site for it. This ensures you have direct control of your data, that the transport is encrypted in a secure way, and that users can reach your site safely without being surveilled. 

## Our Focus on Privacy and Security

As stated, the Clean Insights system is based on and fully compatible with the Piwik.org software and service. We have focused on implementing a number of privacy and security enhancing features and defaults. In addition, our focus is on "measuring" the use of applications to gain "insights", as opposed to "tracking" users for "data". This is a subtle shift in language and perspective, but an important one.

Here are the primary changes in the Clean Insights SDK:

Usage Privacy
* No unique permanent user identifiers are generated or set by default. Static identifiers or session-length random identifiers are used. 
* No notification of download or first use timestamps are stored, set or sent to the server.
* No count of number of uses of the app is stored or sent to the server.

Advanced Privacy
* Built-in implementation of Google's RAPPOR Privacy Preserving Reporting: https://github.com/google/rappor
* Support for threshold-based measurement triggers to reduce the amount of data gathered
* Long lifecycle for dispatch of measurements to server (i.e. send only once per day, week or month)

Network Security
* HTTPS/TLS or a Tor Onion Service (.onion) is required for the backend Piwik instance.
* Certificate pinning is supported and encouraged by default.
* Support for domain-fronting via common cloud services is included/planned, to destination of traffic.
* Support for sending data proxyied over Orbot (Tor for Android) is built-in if the user has it installed and activated.

![Dataflow diagram v1](https://raw.githubusercontent.com/cleaninsights/cleaninsights-android-sdk/master/ux/dataflow.png)

## Getting started

Integrating Clean Insights with Piwik into your Android app
 
1. [Install Piwik](http://piwik.org/docs/installation/)
2. [Create a new website in the Piwik web interface](http://piwik.org/docs/manage-websites/). Copy the Website ID from "Settings > Websites".
3. [Include the library](#include-library)
4. [Initialize Insights](#initialize-insights).
5. [Safely measures import values, exceptions, goals and more](#insights-usage).
6. [Advanced measurement usage](#advanced-measurement-usage)


### Include library
Add this to your apps build.gradle file:

```groovy
dependencies {
    repositories {
        jcenter()
    }
    // ...
    compile 'io.cleaninsights:cleaninsights-android-sdk:1.0.0'
}
```

### Initialize Tracker

#### Basic

You can simply extend your application with a 
[``CleanInsightsApplication``](https://github.com/piwik/piwik-sdk-android/blob/master/piwik-sdk/src/main/java/org/piwik/sdk/PiwikApplication.java) class.
[This approach is used](https://github.com/cleaninsights/cleaninsights-android-sdk/blob/master/exampleapp/src/main/java/io/cleaninsights/demo/DemoApp.java) in our demo app.

#### Advanced

Developers could manage the measurement lifecycle by themselves. To ensure that the metrics are not over-counted, it is highly 
recommended that the Measurer instance be created and managed in the Application class.

```java

import java.net.MalformedURLException;

public class YourApplication extends Application {
    private Measurer mMeasurer;

    public synchronized Measurer getMeasurer() {
        if (mMeasurer != null) {
            return mMeasurer;
        }

        try {
            mMeasurer = CleanInsights.getInstance(this).newMeasurer("http://your-piwik-domain.tld/piwik.php", 1);
        } catch (MalformedURLException e) {
            Log.w(Tracker.LOGGER_TAG, "url is malformed", e);
            return null;
        }

        return mMeasurer;
    }
    //...
}
```

Don't forget to add application name to your `AndroidManifest.xml` file.
 
```xml

    <application android:name=".YourApplication">
        <!-- activities goes here -->
    </application>
```


### Insights Usage

#### Measure screen views

To send a screen view set the screen path and titles on the measurer. Measurement of every screen opened and when could leak certain usages of the app, and should used carefully. That said, Clean Insights will detect if you have already tracked that this screen was opened once, and not report it multiple times per session. This is an appropriate level of usage tracking, as opposed to every time a user opens a screen.

```java

public class YourActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Measurer measurer = ((CleanInsightsApplication) getApplication()).getMeasurer();
        MeasureHelper.measure().screen("/your_activity").title("Title").with(measurer);
    }
}
```

#### Measure events

To gain insights about user's interaction with interactive components of your app, like button presses or the use of a particular item in a game 
use [measureEvent](http://cleaninsights.github.io/cleaninsights-android-sdk/io/cleaninsights/sdk/Measurer.html#measureEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)) 
method.

```java

MeasureHelper.measure().event("category", "action").name("label").value(1000f).with(measurer);
```

#### Measure goals

If you want to trigger a conversion manually or measure some user interaction simply call the method 
[measureGoal](http://cleaninsights.github.io/cleaninsights-android-sdk/io/cleaninsights/sdk/Measurer.html#measureGoal(java.lang.Integer)).
Read more about what is a [Goal in Piwik](http://piwik.org/docs/tracking-goals-web-analytics/).

```java

MeasureHelper.measure().goal(1).revenue(revenue).with(measurer)
```

#### Measure custom vars

To measure a custom name-value pair assigned to your users or screen views use 
[setVisitCustomVariable](http://piwik.github.io/piwik-sdk-android/org/piwik/sdk/Tracker.html#setVisitCustomVariable(int, java.lang.String, java.lang.String))
and
[setScreenCustomVariable](http://piwik.github.io/piwik-sdk-android/org/piwik/sdk/TrackMe.html#setScreenCustomVariable(int, java.lang.String, java.lang.String))
methods. Those methods have to be called before a call to [trackScreenView](#track-screen-views).
More about [custom variables on piwik.org](http://piwik.org/docs/custom-variables/).


```java

Measurer measurer = ((CleanInsightsApplication) getApplication()).getMeasurer();
measurer.setVisitCustomVariable(2, "Age", "99");
MeasurerHelper.measure().screen("/path").variable(2, "Price", "0.99").with(measurer);
```

#### Measure application downloads

To measure the number of app downloads you may call the method [``measureAppDownload``](http://piwik.github.io/piwik-sdk-android/org/piwik/sdk/Tracker.html#trackAppDownload())
This method uses ``SharedPreferences`` to ensures that tracking application downloading will be fired only once.

```java

MeasureHelper.measure().download().with(measure);
```

#### Custom Dimensions
To measure [Custom Dimensions](https://plugins.piwik.org/CustomDimensions) in scope Action or Visit
consider following example:

```java

Measure measurer = ((YourApplication) getApplication()).getMeasurer();
measurer.measure(
    new CustomDimensions()
        .set(1, "foo")
        .set(2, "bar")
);
```


#### Dispatching

The measurer by default will dispatch any pending events every once per day. We user a longer interval of dispatching in order to reduce network surveillance of traffic and app usage.

If a negative value is used the dispatch timer will never run, a manual dispatch must be used:

```java
        
    Measurer measure = ((YourApplication) getApplication()).getMeasurer();
    measure.setDispatchInterval(-1);
    // Measure exception
    try {
        revenue = getRevenue();
    } catch (Exception e) {
        tracker.trackException(e, e.getMessage(), false);
        tracker.dispatch();
        revenue = 0;
    }
    
```

#### User ID

The default behavior of Pwiki is to provide the tracker with a user ID lets you connect data collected from multiple devices and multiple browsers for the same user.  A user ID is typically a non empty string such as username, email address or UUID that uniquely identifies the user.  The User ID must be the same for a given user across all her devices and browsers.

With Clean Insights, we use a static user-id across all clients by default. The user can choose to override this to provide unique per client id's, but we think our default is the most optimal for preservation of privacy.

```java

        ((YourApplication) getApplication()).getMeasurer()
                .setUserId("ffffffffffffff"); //this is our static hexadecimal userid
```

If user ID is used, it must be persisted locally by the app and set directly on the tracker each time the app is started. 

#### Modifying default parameters

The Tracker has a method
[getDefaultTrackMe](http://piwik.github.io/piwik-sdk-android/org/piwik/sdk/Tracker.html#getDefaultTrackMe())
modifying the object returned by it will change the default values used on each query.
Note though that the Tracker will not overwrite any values you set on your own TrackMe object.

#### Detailed API documentation

Here is the design document written to give a brief overview of the SDK project: https://github.com/cleaninsights/cleaninsights-sdk/android/wiki/Design-document

CleanInsights SDK should work fine with Android API Version >= 10 (Android 2.3.3+)

Optional [``autoBindActivities``](https://github.com/piwik/piwik-sdk-android/blob/master/piwik-sdk/src/main/java/org/piwik/sdk/QuickTrack.java)
 method is available on API level >= 14.

Check out the full [API documentation](http://cleaninsights.github.io/cleaninsights-android-sdk/).

#### Debugging

CleanInsights uses [Timber](https://github.com/JakeWharton/timber).
If you don't use Timber in your own app call `Timber.plant(new Timber.DebugTree());`, if you do use Timber in your app then Piwik should automatically participate in your logging efforts.
For more information see [Timbers GitHub](https://github.com/JakeWharton/timber)

### Check SDK

Following command will clean, build, test, generate documentation, do coverage reports and then create a jar.

```
$ ./gradlew :piwik-sdk:clean :piwik-sdk:assemble :piwik-sdk:test :piwik-sdk:jacocoTestReport :piwik-sdk:generateReleaseJavadoc :piwik-sdk:coveralls --info :piwik-sdk:makeJar
```

* Coverage output _./piwik-sdk/build/reports/jacoco/jacocoTestReport/html/index.html_
* Tests report _./piwik-sdk/build/test-report/debug/index.html_
* Javadoc _./piwik-sdk/build/docs/javadoc/index.html_

## Demo application

Browse [the code](https://github.com/cleaninsights/cleaninsights-android-sdk/tree/master/exampleapp) or
build  an .apk by running following command:

```bash
./gradlew :exampleapp:clean :exampleapp:build
```
Generated .apk would be placed in  ``./exampleapp/build/apk/`

## Contribute

* Fork the project
* Create a feature branch based on the 'dev' branch
* Drink coffee and develop an awesome new feature
* Add tests for your new feature
* Make sure that everything still works by running "./gradlew clean assemble test".
* Commit & push the changes to your repo
* Create a pull request from your feature branch against the dev branch of the original repo
* Explain your changes, we can see what changed, but tell us why.
* If your PR passes the travis-ci build and has no merge conflicts, just wait, otherwise fix the code first.

## License

CleanInsights Android SDK for Piwik is released under the BSD-3 Clause license, see [LICENSE](https://github.com/cleaninsights/cleaninsights-android-sdk/blob/master/LICENSE).

