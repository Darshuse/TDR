package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.HashMap;
import java.util.Map;

public abstract class Command {

	// factory for all command types
	private static Map<String, Class<? extends Command>> commandsFactory = new HashMap<String, Class<? extends Command>>();

	/**
	 * generates a command from its type, will return null if the type did not correspond to a registered command
	 * 
	 * @param type
	 * @return
	 */
	public static Command getCommandByType(CommandType type) {
		String typeName = type.toString();
		if (!commandsFactory.containsKey(typeName)) {
			return null;// command not registered
		}

		// lets get the command
		Class<? extends Command> commandClazz = commandsFactory.get(typeName);
		try {
			return commandClazz.getDeclaredConstructor().newInstance(new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		// command creation/instantiation failed
		return null;
	}

	/**
	 * this must be called once for each class that inherits the Command class
	 * 
	 * @param type
	 * @param clazz
	 */
	public static void registerCommandFactory(CommandType type, Class<? extends Command> clazz) {
		commandsFactory.put(type.toString(), clazz);
	}

	/**
	 * get the type of the command
	 * 
	 * @return
	 */
	public abstract CommandType getType();

	/**
	 * replace every ; with \\; in the passed string, thats because we add the separator ;; to the string before passing it to the client/server
	 * 
	 * @param value
	 * @return
	 */
	String hideSpecialChar(String value) {
		return value.replace(";", "\\;");
	}

	/**
	 * this will parse the encrypted command buffer to fill valid data used by the subclass members
	 * 
	 * @param buffer
	 * @throws InvalidDataRecievedException
	 */
	public abstract void parse(String buffer) throws Exception;

	/**
	 * this will convert the class members into an encrypted stream to be sent over the network
	 * 
	 * @return
	 */
	public abstract String prepare() throws Exception;

	/**
	 * replace every \\; with ; in the passed string, thats to revert the changes made by hideSpecialChar()
	 * 
	 * @param value
	 * @return
	 */
	String showSpecialChar(String value) {
		return value.replace("\\;", ";");
	}
}
