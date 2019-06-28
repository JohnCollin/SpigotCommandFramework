# SpigotCommandFramework
A simple framework for Spigot plugins using Java Annotation Capabilities. MC versions [1.14.3+]

# Example Usage
To use, it is very simple. All you must do is create an execution method in any class, add an @Command annotation with label and
possible aliases. Then, with your CommandFramework in your main JavaPlugin class, add a handler object of that class to the list.
NOTE: Do not add the default JavaPlugin, it is added by default.
