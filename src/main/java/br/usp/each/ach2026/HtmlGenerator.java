package br.usp.each.ach2026;

import static j2html.TagCreator.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import j2html.tags.Tag;

public class HtmlGenerator {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
	
	public static String fileNotFound(String fileName) {
		return html().with(
				head().with(
						title("Not Found")
						),
				body().with(
						h1("Not Found"),
						p("The requested URL " + fileName.substring(1)+" was not found on this server.")
						)
				).render();
	}

	public static String listDirectoryContent(String dir) throws Exception {
		return html().with(
				head().with(
						title("Index of " + dir.substring(1)),
						style().withText("table {width: 50%}")
						.withText("td.centered {text-align: center}")
						),
				body().with(
						h1("Index of " + dir.substring(1)),
						table().with(
								tr().with(
										th("Name"),
										th("Last Modified"),
										th("Size")
										)
								).with(createPathList(dir))
						)
				).render();
	}
	
	private static List<Tag> createPathList(String dir) throws Exception {
		return Files.list(Paths.get(dir))
				.map(path ->
				tr().with(
						td().with(
								a(path.getFileName().toString()).withHref(path.toString())
								),
						td(sdf.format(path.toFile().lastModified())).withClass("centered"),
						td(path.toFile().isDirectory()? "-" : 
							readableFileSize(path.toFile().length())).withClass("centered"))
						).collect(Collectors.toList());
	}
	
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
	    
	    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
