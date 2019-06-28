package org.johncollin.spigotcommandframework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandFramework implements CommandExecutor {
	
	private final JavaPlugin instance;
	
	private ArrayList<Object> commandClasses;
	
	private boolean verbose;
	
	private HashMap<Command, Method> methods = new HashMap<Command, Method>();
	
	public CommandFramework(JavaPlugin instance) {
		this.instance = instance;
		
		this.commandClasses = new ArrayList<Object>();
		
		this.verbose = false;
		
		commandClasses.add(instance);
	}
	
	public CommandFramework(JavaPlugin instance, boolean verbose) {
		this.instance = instance;
		
		this.commandClasses = new ArrayList<Object>();
		
		this.verbose = verbose;
		
		commandClasses.add(instance);
	}
	
	/**
	 * Basic initialization method that takes the base class's methods and checks to see if any have the @Command annotation.
	 */
	public void init() {
		for(Object o : commandClasses) {
			for(Method m : o.getClass().getMethods()) {
				Annotation[] annotations = m.getDeclaredAnnotations();
				for(Annotation a : annotations) {
					if(a instanceof Command) {
						Command command = (Command) a;
						methods.put(command, m);
						instance.getCommand(command.label()).setExecutor(this);
					}
				}
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		for(Command c : methods.keySet()) {
			if(c.label().equalsIgnoreCase(cmd.getName())) {
				invokeMethod(c, sender, cmd, label, args);
			}
			for(String alias : c.aliases()) {
				if(alias.equalsIgnoreCase(cmd.getName())) {
					invokeMethod(c, sender, cmd, label, args);
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method that invokes the method selected from the command listed.
	 * 
	 * @param Command Annotation
	 * @param Command Sender
	 * @param Bukkit Command Object
	 * @param Command Arguments
	 */
	private void invokeMethod(Command c, CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		try {
			methods.get(c).invoke(instance, sender, cmd, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a command class to the command framework for handling.
	 * 
	 * @param Command Class Object to call methods from.
	 */
	public void addCommandClass(Object o) {
		commandClasses.add(o);
	}
	
}
