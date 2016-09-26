package br.usp.each.ach2026;

import j2html.tags.Tag;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class HtmlGenerator {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm");

	public static String unauthorized(final String fileName) {
		return html().with(
				head().with(
						title("Unauthorized")
				),
				body().with(
						h1("Unauthorized"),
						p("Access to the requested URL " + fileName.substring(1) + " is Restrict.")
				)
		).render();
	}

	public static String fileNotFound(final String fileName) {
		return html().with(
				head().with(
						title("Not Found")
						),
				body().with(
						h1("Not Found"),
						p("The requested URL " + fileName.substring(1) + " was not found on this server.")
						)
				).render();
	}

	public static String listDirectoryContent(final String dir) throws Exception {
		return html().with(
				head().with(
						title("Index of " + dir.substring(1)),
						style().withText("table{width: 70%} th,td{padding: 5px} td.centered{text-align: center}")
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

	private static List<Tag> createPathList(final String dir) throws Exception {
		return Files.list(Paths.get(dir)).sorted().map(path ->
				tr().with(
						td().with(
								a(path.toFile().isDirectory()? path.getFileName().toString() + "/" :
									path.getFileName().toString()).withHref(path.toString().substring(1))
								),
						td(sdf.format(path.toFile().lastModified())).withClass("centered"),
						td(path.toFile().isDirectory()? "-" : 
							readableFileSize(path.toFile().length())).withClass("centered"))
						).collect(Collectors.toList());
	}

	public static String readableFileSize(final long size) {
		if (size <= 0) return "0";

		final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
		final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
