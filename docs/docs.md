# quick-config documentation

This is a lib mod that helps you create malilib-styled configure gui easily.

## Set up the environment

1. Create a `/lib` folder under your project. Download the jar file of `quick-config` matching your minecraft version from [here](https://github.com/Ivan-1F/quick-config/releases). Put it in the `/lib` folder

2. Add the following lines to the `dependencies` block in the `build.gradle` file:

```groovy
modImplementation files("libs/quick-config-mc1.16.5-1.0.0.jar")     // Replace with the actual filename
```

3. Reload gradle to index new dependencies

4. `quick-config` is now imported to your project

## Get started

Let's start from editing the entry of your mod, open your `ModInitializer`, it is similar to something like this:

```java
public class ExampleQuickConfigMod implements ModInitializer {
    @Override
    public void onInitialize() {
        
    }
}
```

Let the class implement the `QuickConfigExtension` interface, and let the `ExtensionManager` to manage this extension:

```java
public class ExampleQuickConfigMod implements ModInitializer, QuickConfigExtension {     // implement QuickConfigExtension
    @Override
    public void onInitialize() {
        ExtensionManager.manageExtension(this);     // Let the ExtensionManager to manage this extension
    }
}
```

Let's set a hotkey to open the gui:

```java
public class ExampleQuickConfigMod implements ModInitializer, QuickConfigExtension {     // implement QuickConfigExtension
    @Override
    public void onInitialize() {
        ExtensionManager.manageExtension(this);     // Let the ExtensionManager to manage this extension
    }
    @Override
    public String getOpenGuiHotkey() {
        return "R + C";
    }
}
```

You can use `+` to connect each key to create a multi keybinding, it will be trigger when all the keys are pressed in the correct order

## Add a category

Using `quick-config`, you can create a lot of `setting` to your extension. `quick-config` will automatically generate a gui screen that let the player control all the settings and save the user data at `minecraft/config/<Extension Name>.json`

Settings rely on categories. So first, we will add a category to our extension:

Each category is declared by a separate class, it is recommended to keep all the categories to a `categories` package

For instance, create `categories.Tweaks` class, add `@Category` annotation to it to mark it as a category:

```java
@Category
public class Tweaks {

}
```

Then, in the `ModInitializer`, implement `getCategories()` method to register this category:

```java
public class ExampleQuickConfigMod implements ModInitializer, QuickConfigExtension {
    @Override
    public void onInitialize() {
        ExtensionManager.manageExtension(this);
    }

    @Override
    public List<Class<?>> getCategories() {
        return ImmutableList.of(Tweaks.class);  // Register the category
    }

    @Override
    public String getOpenGuiHotkey() {
        return "R + C";
    }
}
```

Now run the game, join a world or connect to a server and open the gui, you will now see the category tabs

![category](https://github.com/Ivan-1F/quick-config/blob/master/screeshots/docs/category.png)

**Note:** There **must** be at least one category, otherwise the client will crash when the player tries to open the gui

## Add a setting

Adding a setting to a category is very simple, simply declare a **static and public** field in the category class and add the `@Setting` annotation to it:

You can access the value of settings in this way: `Tweaks.exampleBooleanSetting`

```java
@Category
public class Tweaks {
    @Setting
    public static boolean exampleBooleanSetting = false;
    @Setting
    public static String exampleStringSetting = "";
}
```

Now run the game, and open the gui, you will now see the settings under your category

![setting](https://github.com/Ivan-1F/quick-config/blob/master/screeshots/docs/setting.png)

### Adding a comment to a setting

If you want to provide detailed information of a setting, you can set `comment` attribute of `@Setting` to `true`:

```java
@Category
public class Tweaks {
    @Setting(comment = true)
    public static boolean exampleBooleanSetting = false;
}
```

Now when the player hovers the name of the setting in the gui, a tip will appear

![comment](https://github.com/Ivan-1F/quick-config/blob/master/screeshots/docs/comment.png)

## Change handlers

If you want to trigger something when a setting's value is changed by the user, you can use the `ChangeHandler`:

```java
@Category
public class Tweaks {
    @Setting(comment = true)
    public static boolean exampleBooleanSetting = false;
    @ChangeHandler(of = "exampleBooleanSetting")    // this should be same as the setting's name you want to listen to
    public static Consumer<Boolean> exampleBooleanSettingChangeHandler = (value) -> {   // Here's the new value of the setting
        // Code here will be triggered when the value of exampleBooleanSetting is changed 
        System.out.println(value);
    };
}
```

## Hotkeys

For boolean settings, it is common to set a hotkey for them. So the player can toggle the setting using hotkeys without opening the gui

Simply add `@WithHotkey` annotation to your setting. Then the player can set a custom hotkey for this setting: 

```java
@Category
public class Tweaks {
    @Setting(comment = true)
    @WithHotkey
    public static boolean exampleBooleanSetting = false;
}
```

![hotkey1](https://github.com/Ivan-1F/quick-config/blob/master/screeshots/docs/hotkey1.png)
![hotkey2](https://github.com/Ivan-1F/quick-config/blob/master/screeshots/docs/hotkey2.png)
![hotkey3](https://github.com/Ivan-1F/quick-config/blob/master/screeshots/docs/hotkey3.png)

## Translation

You may notice that all the texts in the gui are showed as translation keys. So you can provide different languages for your extension easily

`quick-config` will translate all the text to formatted translation keys:

|Text|Translation Key|Explanation|Example|Snapshot|
|---|---|---|---|---|
|Extension name|\<extensionName\>.name|\<extensionName\> is the name of the entry class of the extension (which implements the `QuickConfigExtension` interface), will be converted to lower camel case|clientTweaks.name||
|Category name|category.\<categoryName\>.name|\<categoryName\> is the name of the category class, will be converted to lower camel case|category.tweaks.name||
|Setting name|setting.\<categoryName\>.\<settingName\>.name|\<settingName\> is the name of the setting field|setting.tweaks.fastRightClick.name||
|Setting comment|setting.\<categoryName\>.\<settingName\>.comment|The same as above|setting.tweaks.fastRightClick.comment||

Just follow the [Fabric wiki](https://fabricmc.net/wiki/tutorial:lang) to create lang files, then the text will be displayed correctly