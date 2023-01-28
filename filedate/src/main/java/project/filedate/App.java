package project.filedate;

import java.io.BufferedReader;
import java.io.File;

import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

	private static Logger logger = Logger.getLogger(App.class.getSimpleName());

	public static void main(String[] args) throws Exception {

		String filePath = getInputFromUser();
		File f1 = new File(filePath);

		FileTime f1FileTimeV1 = (FileTime) Files.getAttribute(f1.toPath(), "creationTime");
		FileTime f1FileTimeV2 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class).creationTime();

		logger.log(Level.INFO, "#DATA# (java.nio.file: Files.getAttribute): {0} ", f1FileTimeV1);
		logger.log(Level.INFO, "#DATA# (java.nio.file: Files.readAttributes): {0} ", f1FileTimeV2);

		if (getOperatingSystemDetails().contains("Linux")) {
			String f1r = executeStatCommand(f1.getAbsolutePath());
			logger.log(Level.INFO, "#DATA# (Linux stat command):  {0}", getDateFromCommandExecution(f1r));
		}
	}

	/**
	 * Allows the user to input into console
	 * 
	 * @return the string entered by the user
	 */
	private static String getInputFromUser() {
		logger.log(Level.INFO, "Incolla il path ad un file e premi invio");
		String filePath = "";
		try (Scanner scan = new Scanner(System.in);) {
			filePath = scan.next();
		}
		return filePath;
	}

	/**
	 * Extracts the date from the string returned by the 'stat' command using a
	 * regex
	 * 
	 * @param stringResult the result of the 'stat' command
	 * @return the date extracted
	 */
	private static String getDateFromCommandExecution(String stringResult) {
		String regex = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1]) (2[0-3]|[01]\\d):[0-5]\\d:[0-5]\\d";
		Pattern pattern = Pattern.compile(regex);
		String[] lines = stringResult.split("\\n");

		for (String line : lines) {
			if (line.trim().startsWith("Modify:") && (line.trim().endsWith("00"))) {

				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {
					return matcher.group();

				}
			}
		}
		return "";
	}

	/**
	 * Runs the linux 'stat' command on the specified path
	 * 
	 * @param filepath path of the file to be analyzed
	 * @return result of executing the command
	 * @throws Exception
	 */
	public static String executeStatCommand(String filepath) throws Exception {
		List<String> params = new ArrayList<>();
		params.add("stat");
		params.add(filepath);
		return executeCommand(params);
	}

	private static String executeCommand(List<String> commandParams) throws Exception {

		Process process = null;

		ProcessBuilder builder = new ProcessBuilder(commandParams);
		builder.redirectErrorStream(true);
		process = builder.start();

		StringBuilder result = new StringBuilder();
		String line;

		try (InputStreamReader reader = new InputStreamReader(process.getInputStream());
				BufferedReader in = new BufferedReader(reader)) {

			while ((line = in.readLine()) != null) {
				result.append(line).append(System.getProperty("line.separator"));
			}

		} finally {
			process.waitFor();
			process.destroy();
		}
		return result.toString();
	}

	/**
	 * This Method returns the current Operating System
	 * 
	 * @return the OS name as a String
	 */
	public static String getOperatingSystemDetails() {
		return System.getProperty("os.name");
	}
}
