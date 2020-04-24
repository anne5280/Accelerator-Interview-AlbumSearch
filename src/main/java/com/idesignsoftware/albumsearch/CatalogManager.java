package com.idesignsoftware.albumsearch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;

/*
 * The responsibility of this class is to load the album catalog.
 *
 * This class can be re-factored to be it's own service.  The benefits of doing that are the following:
 *   1. Real-time very large data set management
 *   2. Real-time CRUD across many users
 *   3. Real-time view management ; this means that when the user moves between viewing one set of entries to the next, the available records may change.
 *   4. As it's own service, it can use threads to tally information about the data in real-time (aka, statistics on use)(...or maybe analytics should it's own service).
 *   
 */
public class CatalogManager {
	
	/*
	 * Using an ArrayList means the manager can maintain a sliding window range into the catalog.
	 * The view segment can be configurable based on tuning and analytics.
	 * @TODO These settings can be configurable through properties.
	 */
	public static final int PAGE = 10;
	private int start = 0;
	private int end = start + PAGE;
	private ArrayList<Item> catalog = new ArrayList<Item>(PAGE);	

	private int lastIndex = 0;
	
	class Item {
		String album ;
		String artist;
		String genre;
		String year;
		
		Item(List<String> record) {
			album = record.get(0);
			artist = record.get(1);
			genre = record.get(2);
			year = record.get(3);
		}
		
		public String toString() {
			return album + "," + artist + "," +  genre + "," +  year;

		}
		
		public List<String> getColumns() {
			List<String> columns = new ArrayList<String>(4);
			columns.add(album);
			columns.add(artist);
			columns.add(genre);
			columns.add(year);
			
			return columns;
		}
		
		public void updateItem(List<String> record) {
			setAlbum(record.get(0));
			setArtist(record.get(1));
			setGenre(record.get(2));
			setYear(record.get(3));
		}
		
		public void updateItem(String album, String artist, String genre, String year) {
			setAlbum(album);
			setArtist(artist);
			setGenre(genre);
			setYear(year);
		}

		public String getAlbum() {
			return album;
		}

		public void setAlbum(String album) {
			this.album = album;
		}

		public String getArtist() {
			return artist;
		}

		public void setArtist(String artist) {
			this.artist = artist;
		}

		public String getGenre() {
			return genre;
		}

		public void setGenre(String genre) {
			this.genre = genre;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}
		
	}

	/* @TODO This class should probably be a Singleton :
		         private static class HoldInstance {
        			private static final Singleton INSTANCE = new Singleton();
    				}
 
    			public static Singleton getInstance() {
        			return HoldInstance.INSTANCE;
    			}
	*/      
	
	/* @TODO This class should throw an exception if the load call fails. */
	public CatalogManager() {
		loadAlbumCatalog();
	}
	
	/*
	 * The get page methods maintain produce sliding window range into the catalog.
	 * @TODO Do the page get functions need to be thread safe???
	 * @TODO What if the catalog is empty???
	 */
	private List<Item> getCurrentPage() {
		ArrayList<Item> catalogPage = new ArrayList<Item>(PAGE+1);
		
		for (int i=start ; i <= end ; i++ )
			catalogPage.add(catalog.get(i));
		
		return catalogPage;
	}
	
	public List<Item> getNextPage() {
		
		end = (end + PAGE <= lastIndex) ? end + PAGE : lastIndex;
		start = end - PAGE;
		
		return getCurrentPage();
	}
	
	public List<Item> getPrevPage() {
		
		start = start - PAGE >= 0 ? start - PAGE : 0; 
		end = start + PAGE;
		
		return getCurrentPage();
	}	
	


	private void loadAlbumCatalog() {
		try {
			InputStream albumFile = ContextListener.class.getClassLoader().getResourceAsStream("albums.csv");
			BufferedReader r = new BufferedReader(new InputStreamReader(albumFile));
	        String line;
            
            while ((line = r.readLine()) != null) {
            	catalog.add(new Item(getColumns(line)));		
            	System.out.println(line);
            }
			
            lastIndex = catalog.size() - 1;
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}		
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	// @TODO handle for null
	public String getPage(String direction) {
		List<Item> segment = new ArrayList<Item>();  // new ArrayList<Item>();
		
		if (direction == null) {
			getPage();
		} else if ( direction.equalsIgnoreCase("next") ) 
			segment = getNextPage();
		else
			segment = getPrevPage();
		
		return toJSON(segment);
	}
	
	public String getPage() {		
		return toJSON(getCurrentPage());
	}
	
	private List<String> getColumns(String row) {
		String colstr;
		String[] title = row.split("\",");
		List<String> columns = new ArrayList<String>();
		if ( title.length > 1 ) {
			columns.add(title[0].replace("\"", ""));
			colstr = title[1];
		} else {
		
			colstr = title[0];
		}
		
		String[] rowColumns = colstr.split(",");
		for ( int t = 0 ; t < rowColumns.length ; t++)
			columns.add(rowColumns[t]);
		
		return columns;
	}
	
	// @TODO home-grown JSON string lands in ajax.failure, but JSON would enable Bootstrap table widget
	// This is a workaround.
	public String toJSON(List<Item> tableSeg) {
		String widget = "<div>";
		
		String onclick;
		List<String> columns = catalog.get(0).getColumns();
		
		// @TODO Ideally, the table header should not be reloaded every time the user scrolls		
		widget = widget.concat("<table class=\"table table-striped\">");
		widget = widget.concat("<thead>");
		widget = widget.concat("<tr>");
		for (int c = 0 ; c < columns.size() ; c++) {
			onclick = "<th scope=\"col\" onclick='updateAlbum("+ c + ");'>";
			widget = widget.concat(onclick);
			widget = widget.concat(columns.get(c));
			widget = widget.concat("</th>");
		}
		widget = widget.concat("</tr>");
		widget = widget.concat("</thead>");
		
		widget = widget.concat("<tbody>");
		for (int idx=1, row = start+idx ; idx <= tableSeg.size()-1 ; idx++, row++ ) {
			columns = tableSeg.get(idx).getColumns();
			
			widget = widget.concat("<tr>");
			widget = widget.concat("<th scope=\"row\" onclick='updateAlbum(");
			widget = widget.concat(row + ");'>");			// update handler
			widget = widget.concat(columns.get(0));
			widget = widget.concat("</th>");
			for (int col = 1 ; col < columns.size() ; col++) {
				widget = widget.concat("<td>");
				widget = widget.concat(columns.get(col));
				widget = widget.concat("</td>");
			}
			widget = widget.concat("</tr>");
		}
		widget = widget.concat("</tbody>");
		widget = widget.concat("</table>");
		widget = widget.concat("</div>");
		
		return widget;
	}
	
	public String getArtistList() {
		String widget = "<div>";
		
		// @TODO Ideally, the table header should not be reloaded every time the user scrolls		
		widget = widget.concat("<select multiple class=\"form-control\" id=\"artistList\">");
		
		widget = widget.concat("<tbody>");
		for (int idx=1 ; idx < catalog.size() ; idx++) {
			widget = widget.concat("<option onclick='showWork(\"");
			widget = widget.concat(catalog.get(idx).getArtist() + "\");'>");	
			widget = widget.concat(catalog.get(idx).getArtist());
			widget = widget.concat("</option>");
		}
		widget = widget.concat("</tbody>");
		widget = widget.concat("</select>");
		
		widget = widget.concat("</div>");
		
		return widget;
	}	
    
	
	public String getArtistWork(String artist) {
		String widget = "<div>";
		
		List<Item> albums = catalog.stream()
				.filter(item -> item.artist.equalsIgnoreCase(artist))
				.collect(Collectors.toList());
		
		for (Item album : albums) {
			widget = widget.concat("<div>");
			widget = widget.concat(album.toString());
			widget = widget.concat("</div>");
		}
		widget = widget.concat("</div>");
		
		return widget;
	}	
	
	public String newAlbum(HttpServletRequest request) {

		String record = request.getParameter("album");
		record = record.concat(",");
		record = record.concat(request.getParameter("artist"));
		record = record.concat(",");
		record = record.concat(request.getParameter("genre"));
		record = record.concat(",");
		record = record.concat(request.getParameter("year"));
		
		catalog.add(1, new Item(getColumns(record)));	
        lastIndex = catalog.size() - 1;
		
		return "Success";
	}	
	
	public String updateAlbum(HttpServletRequest request) {

		int recordId = Integer.parseInt(request.getParameter("uprecord"));		
		Item album = catalog.get(recordId);
		album.updateItem(request.getParameter("upalbum"), request.getParameter("upartist"), request.getParameter("upgenre"), request.getParameter("upyear"));
		catalog.set(recordId, album);	
		
		return "Success";
	}
	
	public String getAlbum(String recordId) {
		int id = Integer.parseInt(recordId);
		String record = recordId + "," + catalog.get(id).toString();
		return record;	
	}	
	
	public String deleteAlbum(HttpServletRequest request) {
		catalog.remove(Integer.parseInt(request.getParameter("uprecord")));
		return "Success";
	}
	
	//Create an endpoint that returns genres ranked by number of albums or list the years with the most albums.
	public String rankGenres() {
		
		Map<String, List<Item>> albums = catalog.stream()
				.collect(Collectors.groupingBy(Item::getGenre));
		
		List<String> ranks = albums.entrySet().stream()
			.sorted((e1, e2) -> {
	            return e1.getValue().size() > e2.getValue().size() ? 1 : 0;
	        })
			.map(el -> el.getKey())
			.filter(g -> !g.equalsIgnoreCase("genre"))
	        .collect(Collectors.toList());
		
		String widget = "<div>";
		
		for (String genre : ranks) {
			widget = widget.concat("<div>");
			widget = widget.concat(genre);
			widget = widget.concat("</div>");
		}
		widget = widget.concat("</div>");
		
		return widget;
		
	}

}
