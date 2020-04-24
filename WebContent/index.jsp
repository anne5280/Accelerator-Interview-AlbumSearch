<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="com.idesignsoftware.albumsearch.*"
    import="java.util.*"
    import="java.util.stream.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Album Search Dashboard</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
<!-- <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script> -->
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script> -->
<!-- <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script> -->
<!-- <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"> -->

<script type="text/javascript" src="js/jquery-3.3.1.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css">

<link href="css/cognizant.css" rel="stylesheet">
 </head>

<body>
	<div class="row">
	    <div class="col" >
	        <img class="recordspin zi1" src="images/record.png">
	    </div>	
		<div class="col">
   			Coding Test, A. George, Pie Day, 2019
   		</div>
   	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-8">
				<div id="table"></div>
			</div>
			<div class="col-md-4">
				<div id="updatetable">
				
			        <form id="updateform">
			        	<input type=hidden class="form-control" id="uprecord">
						<div class="form-group">
						    <label for="udalbum">Album</label>
						    <input type="text" class="form-control" id="upalbum" placeholder="Album Name">
						  </div>
						  <div class="form-group">
						    <label for="udartist">Artist</label>
						    <input type="text" class="form-control" id="upartist" placeholder="Artist">
						  </div>
						  <div class="form-group">
						    <label for="udartist">Genre</label>
						    <input type="text" class="form-control" id="upgenre" placeholder="Genre">
						  </div>
						  <div class="form-group">
						    <label for="udyear">Year</label>
						    <input type="text" class="form-control" id="upyear" placeholder="Year">
						  </div>					  					  
					  
					  	  <button type="submit" id="updatesubmit" class="btn btn-primary">Update Album Title</button>
					  	  <button type="button" id="deletesubmit" class="btn btn-primary">Delete Album Title</button>
					</form>				
				</div>
			</div>
		</div>
		<div class="row m-1">
			<div class="btn-group" role="group" aria-label="Get Record List">
			  <button id="prev" type="button" class="btn btn-secondary" onclick="pageCommand('prev');">Previous</button>
			  <button id="next" type="button" class="btn btn-secondary" onclick="pageCommand('next');">Next</button>
			</div>
		</div>
	    <div class="row align-items-bottom">
			<div id="error" class="col-sm-12 alert alert-danger" role="alert"></div>
		</div>		
	    <div class="row m-3">
	        <div class="col-2 border rounded">
	        	<label for="artistList">New Album</label>
		        <form id="addalbum">
					<div class="form-group">
					    <label for="album">Album</label>
					    <input type="text" class="form-control" id="album" placeholder="Album Name">
					  </div>
					  <div class="form-group">
					    <label for="artist">Artist</label>
					    <input type="text" class="form-control" id="artist" placeholder="Artist">
					  </div>
					  <div class="form-group">
					    <label for="artist">Genre</label>
					    <input type="text" class="form-control" id="genre" placeholder="Genre">
					  </div>
					  <div class="form-group">
					    <label for="year">Year</label>
					    <input type="text" class="form-control" id="year" placeholder="Year">
					  </div>					  					  
				  
				  	  <button type="submit"  id="addalbum" class="btn btn-primary">Save New Title</button>
				</form>
	        </div>
	        <div class="col-4 border ">
	        	<label for="album">Artist List</label>
		        <form id="addalbum">
					  <div class="form-group">
					    <label for="artistList">Artist</label>
					  	<div id="artistList">
						</div>					    
					  </div>
				</form>
				<div>
					<label for="album">Career</label>
					<div id="artistReview"></div>
				</div>
	        </div>	   
	        <div class="col-5">
	          <label for="album">Genre, by Album Count</label>
			  <div id="artistgenres" class="form-group">
			  </div>
	        </div>
    	</div>

   	</div>
</body>

<script type="text/javascript">

	  // Show the catalog
	  var ajaxResult = function(data) {  
		$('#table').html(data);
	  };
	  
	  var ajaxArtists = function(data) {  
			$('#artistList').html(data);
	  };	
	  
	  var ajaxArtistWork = function(data) {  
			$('#artistReview').html(data);
	  };	 
	  
	  var ajaxGenres = function(data) {  
			$('#artistgenres').html(data);
	  };	  
	  
	  var ajaxUpdate = function(data) {  
			console.log("Album updated successfully.");
			initTable();
	  };
	  
	  var ajaxNew = function(data) {  
			console.log("Album created.");
			initTable();
	  };  
	  
	  var ajaxDelete = function(data) {  
			console.log("Album deleted.");
			initTable();
	  };  
	  
	  var ajaxAlbum = function(data) {  
			console.log("Album retrieved.");
			console.log(data);
			var array = data.split(','); 
			document.getElementById("uprecord").value = array[0];
			document.getElementById("upalbum").value = array[1];
			document.getElementById("upartist").value = array[2];
			document.getElementById("upgenre").value = array[3];
			document.getElementById("upyear").value = array[4];
	  }; 
	  
	  var ajaxFailed = function(request, status, error) {
	      if(status == 'parsererror' || status == 'error') {
		        document.getElementById("error").innerHTML += JSON.stringify("status = "+request.statusText);
	      }
	  };
			  
	  var pageCommand = function(direction) {
	
		  $.ajax({
		    url : 'Catalog',
		    method : 'GET',
		    contentType: 'application/json; charset=utf-8',
		    data : {
		    	"direction" : direction,
		    },
		    dataType: 'html',
		    cache: false,
		    success: ajaxResult,
		    error: ajaxFailed,
	
		});    
	  };
	  
	  var getAlbum = function(recordId) {
		  
		  $.ajax({
		    url : 'Catalog',
		    method : 'GET',
		    contentType: 'application/json',
		    data : { "recordId" : recordId},
		    dataType: 'html',
		    cache: false,
		    success: ajaxAlbum,
		    error: ajaxFailed,
			});   
	  };
	  
	  var getArtists = function() {
		  
		  $.ajax({
		    url : 'Catalog',
		    method : 'GET',
		    contentType: 'application/json',
		    data : { "artistlist" : "artistlist"},
		    dataType: 'html',
		    cache: false,
		    success: ajaxArtists,
		    error: ajaxFailed,
			});   
	  };	  
	  getArtists();
	  
	  var rankGenres = function() {
		  
		  $.ajax({
		    url : 'Catalog',
		    method : 'GET',
		    contentType: 'application/html',
		    data : { "genrelist" : "genrelist"},
		    dataType: 'html',
		    cache: false,
		    success: ajaxGenres,
		    error: ajaxFailed,
			});   
	  };	  
	  rankGenres();	  
	  
	  var updateAlbum = function(recordId) {
		  console.log("Get album details from server.");
		  $('#updatetable').toggle();
		  getAlbum(recordId);
	  };
	  
	  var updateAlbumSave = function(data) {
		  console.log("Save album changes.");	  
		  // PUT does not send params
		  var url = 'Catalog'+data;
		  
		  $.ajax({
		    url : url,
		    method : 'PUT',
		    success: ajaxUpdate,
		    error: ajaxFailed,
	
		});  
		  
		$('#updatetable').toggle();
	  };
	  
	  var deleteAlbum = function(data) {
		  console.log("Delete album.");	  
		  var url = 'Catalog'+data;
		  $.ajax({
			url : url,
		    method : 'DELETE',
		    data : data,
		    success: ajaxDelete,
		    error: ajaxFailed,
	
		});  
		  
		$('#updatetable').toggle();
	  };	  
	  
	  var newAlbumSave = function(data) {
		  console.log("Save album changes.");	
		  var url = 'Catalog'+data;
		  $.ajax({
		    url : url,
		    method : 'POST',
		    contentType: 'application/json; charset=utf-8',
		    data : JSON.stringify(data),
		    dataType: 'html',
		    cache: false,
		    success: ajaxNew,
		    error: ajaxFailed,
	
		});  
		  
		$('#updatetable').toggle();
	  };
			  
	  var initTable = function() {
		  $.ajax({
			    url : 'Catalog',
			    method : 'GET',
			    contentType: 'application/json; charset=utf-8',
			    dataType: 'html',
			    cache: false,
			    success: ajaxResult,
			    error: ajaxFailed,
	
			});    
	  };
  
	initTable();
	$('#updatetable').toggle();
 
	document.getElementById("updateform").addEventListener( "submit", function( e ) {
		e.preventDefault();
		var columns = "?uprecord="+document.getElementById("uprecord").value +
			"&upalbum="+document.getElementById("upalbum").value +
			"&upartist="+document.getElementById("upartist").value +
			"&upgenre="+document.getElementById("upgenre").value +
			"&upyear="+document.getElementById("upyear").value;
		updateAlbumSave(columns);

	}, false);
	
	document.getElementById("addalbum").addEventListener( "submit", function( e ) {
		e.preventDefault();
		
		var columns = "?&album="+document.getElementById("album").value +
		"&artist="+document.getElementById("artist").value +
		"&genre="+document.getElementById("genre").value +
		"&year="+document.getElementById("year").value;
		
		newAlbumSave(columns);

	}, false);	
	
	document.getElementById("deletesubmit").addEventListener( "click", function( e ) {
		e.preventDefault();
		
		var columns = "?uprecord="+document.getElementById("uprecord").value;

		deleteAlbum(columns);

	}, false);	
	
	function showWork(artist) {
		  $.ajax({
			    url : 'Catalog',
			    method : 'GET',
			    contentType: 'application/json',
			    data : { "artistwork" : artist},
			    dataType: 'html',
			    cache: false,
			    success: ajaxArtistWork,
			    error: ajaxFailed,
				});  
	}


</script>
</html>