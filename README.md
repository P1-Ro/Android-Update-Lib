
# Android-Update-Lib [![](https://jitpack.io/v/P1-Ro/Android-Update-Lib.svg)](https://jitpack.io/#P1-Ro/Android-Update-Lib) [![](https://jitci.com/gh/P1-Ro/Android-Update-Lib/svg)](https://jitci.com/gh/P1-Ro/Android-Update-Lib)  
  
Easy to setup library for [Android-Update-Server](https://github.com/P1-Ro/Android-Update-Server) that manages updates for your Android app which cannot be distributed via Google Play or you just simply don't want to use it.

## Installation
1. Set-up your own instance of [Android-Update-Server](https://github.com/P1-Ro/Android-Update-Server)  
2. Add Android App to same Firebase project which you are using for Update Server  
3. Include this library in your project
4. Add it to your build.gradle with:
    ```gradle
	allprojects {
	    repositories {
	        maven { url "https://jitpack.io" }
	    }
	}
	```
      and:
      
    ```gradle
   dependencies {
       implementation 'com.github.P1-Ro:Android-Update-Lib:1.1.0'
	}
	 ```
5. Set `url` and `apiKey` of your server in your `strings.xml`  
```xml  
<string name="update_url" translatable="false">URL_HERE</string>
<string name="update_api_key" translatable="false">API_KEY_HERE</string>  
```  

## Initialization

Initialize the Updater in your application’s main `onCreate` method:
```java
import sk.p1ro.Updater;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Updater.init(this);
    }
}
```

## Usage

### Rolling out updates
After you upload new version to update server users will get notified about new version automatically via notification.

### User checks for updates
If you want to allow user to check for updates themselves you can have two options.

#### Option 1
Install update automatically right after check like this:
```java
Button button = findViewById(R.id.updateButton);
button.setOnClickListener(e -> UpdateUtil.checkAndInstallUpdate(this))
```

#### Option 2
You can control individual step when to do one of update steps :
```java
public static void installUpdate(Context context) {
    checkUpdate(context, shouldInstall -> {
        if (shouldInstall) {

            // you can display some dialog here
            downloadUpdate(context, callback -> {

                // you can display some dialog here
                installUpdate(context);
            });
        }
    });
}
```

## Customization
If you want to translate messages you can do so by overriding following strings in your `strings.xml`
```xml
<string name="update_check">Checking updates</string>  
<string name="update_wait_please">This can take a while…</string>  
<string name="update_version">New version %1$s is available</string>  
<string name="update_title">Update available</string>  
<string name="update_latest_version">You have latest version</string>
```
