package com.idesignsoftware.albumsearch;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Catalog
 */
@WebServlet(
		urlPatterns = { "/Catalog" }, 
		initParams = { 
				@WebInitParam(name = "AlbumCatalog", value = "albums.csv", description = "List of albums with details.")
		})
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final CatalogManager manager = new CatalogManager();
	
	private final String DIRECTION = "direction";
	
    /**
     * Default constructor. 
     */
    public Catalog() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get...");
		
		response.setHeader("Pragma",  "no-chache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.setContentType("text/html;charset=UTF-8");			// @TODO setContentType("application/json; charset=UTF-8");
		String direction = request.getParameter(DIRECTION);
		String recordId = request.getParameter("recordId");
		if ( request.getParameter("artistlist") != null ) {
			response.getWriter().append(manager.getArtistList());	
			
		} else if ( request.getParameter("artistwork") != null ) {
			response.getWriter().append(manager.getArtistWork(request.getParameter("artistwork")));	
			
		} else if ( request.getParameter("genrelist") != null ) {
			response.getWriter().append(manager.rankGenres());	
			
		} else if ( recordId != null ) {
			response.getWriter().append(manager.getAlbum(recordId));
			
		} else if ( direction != null ) {
			response.getWriter().append(manager.getPage(direction)); 
			
		} else {
			response.getWriter().append(manager.getPage());  
		}
			
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Post...");
		
		response.setHeader("Pragma",  "no-chache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.setContentType("text/html;charset=UTF-8");			// @TODO setContentType("application/json; charset=UTF-8");
		response.getWriter().append(manager.newAlbum(request));	
		response.flushBuffer();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Post...");
		
		response.setHeader("Pragma",  "no-chache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.setContentType("text/html;charset=UTF-8");		
		response.getWriter().append(manager.updateAlbum(request));	
		response.flushBuffer();
	}	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Post...");
		
		response.setHeader("Pragma",  "no-chache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.setContentType("text/html;charset=UTF-8");		
		response.getWriter().append(manager.deleteAlbum(request));	
		response.flushBuffer();
	}		
}
