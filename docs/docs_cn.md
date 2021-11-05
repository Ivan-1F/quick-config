# quick-config 文档

这是一个库模组来帮助你快速构建 `malilib` 风格的配置界面

## 配置开发环境

1. 在项目根目录下创建 `/lib` 文件夹. 从 [这里](https://github.com/Ivan-1F/quick-config/releases) 下载与 Minecraft 版本相符的 `quick-config` jar 文件，将它放入 `/lib` 文件夹

2. 在 `build.gradle` 文件下的 `dependencies` 代码块中添加下列内容：

```groovy
modImplementation files("libs/quick-config-mc1.16.5-1.0.0.jar")     // 替换为实际的文件名
```

3. 重载 gradle 项目来索引新依赖

4. `quick-config` 已被成功引入你的项目

## 快速上手

让我们从修改 mod 的主入口文件开始，打开你的 `ModInitializer`，类似于下面这样：

```java
public class ExampleQuickConfigMod implements ModInitializer {
    @Override
    public void onInitialize() {
        
    }
}
```

让这个类实现 `QuickConfigExtension` 接口，并让 `ExtensionManager` 托管这个插件：

```java
public class ExampleQuickConfigMod implements ModInitializer, QuickConfigExtension {     // implement QuickConfigExtension
    @Override
    public void onInitialize() {
        ExtensionManager.manageExtension(this);     // Let the ExtensionManager to manage this extension
    }
}
```

让我们设置一个打开 gui 的快捷键：

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

你可以使用 `+` 连接多个按键来创建一个组合键，它会在所有按键都被以正确的顺序按下时触发

## 添加一个分类

使用 `quick-config`, 你可以向你的扩展创建很多 `setting (选项)`。 `quick-config` 会自动生成一个 gui 界面，玩家可以在那里控制所有的选项。用户数据会被存放在 `minecraft/config/<扩展名>.json`

选项依赖于分类，所以首先，我们将为我们的扩展添加一个分类

每一个分类都由一个分离的类定义，推荐将所有分类放在 `cateogries` 包下

例如，创建 `categories.Tweaks` 类，为其添加 `@Category` 来把它标注为一个分类

```java
@Category
public class Tweaks {

}
```

接着，在 `ModInitializer` 中实现 `getCategories()` 方法来注册这个分类

```java
public class ExampleQuickConfigMod implements ModInitializer, QuickConfigExtension {
    @Override
    public void onInitialize() {
        ExtensionManager.manageExtension(this);
    }

    @Override
    public List<Class<?>> getCategories() {
        return ImmutableList.of(Tweaks.class);  // 注册这个分类
    }

    @Override
    public String getOpenGuiHotkey() {
        return "R + C";
    }
}
```

现在运行游戏，加入一个世界或连接到一个服务器，打开 gui，你将会看到分类选项卡

![category](https://github.com/Ivan-1F/quick-config/blob/fabric-1.15.2/screenshots/docs/category.png&raw=true)

**注意：** 一个扩展**必须**存在至少一个分类，不然玩家尝试打开 gui 时客户端会崩溃

## 添加一个选项

向分类添加一个选项非常简单，声明一个 **静态的公开** 变量并为其添加 `@Setting` 注解：

你可以用这种方式访问选项的值：`Tweaks.exampleBooleanSetting`

```java
@Category
public class Tweaks {
    @Setting
    public static boolean exampleBooleanSetting = false;
    @Setting
    public static String exampleStringSetting = "";
}
```

现在运行游戏，打开 gui，你将会在这个分类下看到这个选项

![setting](https://github.com/Ivan-1F/quick-config/blob/fabric-1.15.2/screenshots/docs/setting.png&raw=true)

### 向选项添加注释

如果你想对一个选项提供更详细的说明，你可以将 `@Setting` 注解的 `comment` 属性设为 `true`

```java
@Category
public class Tweaks {
    @Setting(comment = true)
    public static boolean exampleBooleanSetting = false;
}
```

现在当玩家将鼠标移过选项的名称时，一段提示将会出现

![comment](https://github.com/Ivan-1F/quick-config/blob/fabric-1.15.2/screenshots/docs/comment.png&raw=true)

## 选项改变事件

如果你想在一个选项的值被改变时触发些什么，你可以使用 `ChangeHandler`：

```java
@Category
public class Tweaks {
    @Setting(comment = true)
    public static boolean exampleBooleanSetting = false;
    @ChangeHandler(of = "exampleBooleanSetting")    // 应该与你希望监听的选项名称相同
    public static Consumer<Boolean> exampleBooleanSettingChangeHandler = (value) -> {   // 这是选项的新值
        // 这里的代码会在 exampleBooleanSetting 的值改变时被触发 
        System.out.println(value);
    };
}
```

## 快捷键

对于布尔类型的选项，为它们设置一个快捷键控制非常常见。这样玩家就可以在不打开 gui 的情况下切换选项

只需为你的选项添加 `@WithHotkey` 注解，玩家就可以为这个选项设置自定义快捷键了

```java
@Category
public class Tweaks {
    @Setting(comment = true)
    @WithHotkey
    public static boolean exampleBooleanSetting = false;
}
```

![hotkey1](https://github.com/Ivan-1F/quick-config/blob/fabric-1.15.2/screenshots/docs/hotkey1.png?raw=true)
![hotkey2](https://github.com/Ivan-1F/quick-config/blob/fabric-1.15.2/screenshots/docs/hotkey2.png?raw=true)
![hotkey3](https://github.com/Ivan-1F/quick-config/blob/fabric-1.15.2/screenshots/docs/hotkey3.png?raw=true)

## 翻译

你可能注意到了，gui 中所有的文字都被显示为了翻译键。所以你可以方便地为你的扩展提供多种语言支持

`quick-config` 将会把所有的显示的文字转换为格式化过的翻译键：

|文字|翻译键|解释|例子|截图|
|---|---|---|---|---|
|扩展名称|\<扩展名\>.name|\<扩展名\> 为扩展入口类（实现 `QuickConfigExtension` 接口）的名称，转换为小驼峰命名|clientTweaks.name||
|分类名称|category.\<分类名称\>.name|\<分类名称\> 为分类类的名称，转换为小驼峰命名|category.tweaks.name||
|选项名称|setting.\<分类名称\>.\<选项名称\>.name|\<选项名称\> 为选项变量的名称|setting.tweaks.fastRightClick.name||
|选项注释|setting.\<分类名称\>.\<选项名称\>.comment|同上|setting.tweaks.fastRightClick.comment||

根据 [Fabric wiki](https://fabricmc.net/wiki/tutorial:lang) 创建 lang 文件，文字就会正确显示了