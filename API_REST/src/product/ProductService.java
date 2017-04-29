package product;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.security.*;
import javax.interceptor.Interceptors;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.oracle.jrockit.jfr.RequestDelegate;

import console.Console;
import genre.Genre;
import publisher.Publisher;
import user.User;
import user.UserDatabase;
import videogame.VideoGame;

@Path("/products-service")
public class ProductService
{
	@PermitAll
	@GET
	@Path("/products/{id}")
	public Response getProductById(@PathParam("id") int id, @Context HttpRequest request)
	{
		Product product = ProductDatabase.findProductByID(id);

		ResponseBuilder rb;
		if(product == null)
		{
			rb = Response.serverError().status(404);
		}
		else
		{
			rb = Response.ok(product.getProperties());
		}		
		return rb.build();
	}

	@RolesAllowed({"ADMIN"})
	@POST
	@Path("/products/{id_videogame}/{id_console}/{price}/{date}")
	public Response newProduct(@PathParam("id_videogame") int id_videogame,
			@PathParam("id_console") int id_console,
			@PathParam("price") int price,
			@PathParam("date") String date,
			@Context HttpRequest request)
	{
		VideoGame videogame = ProductDatabase.findVideoGameByID(id_videogame);
		Console console = ProductDatabase.findConsoleByID(id_console);
		
		ResponseBuilder rb = null;
		
		if(videogame!= null && console!=null)
		{
			Product product = ProductDatabase.findProductByKey(videogame, console);			
			if(product != null)
			{
				rb = Response.serverError().status(403);
			}
			else
			{
				product = new Product();
				product.setConsole(console);
				product.setDate_release(date);
				product.setPrice(price);
				product.setVideogame(videogame);

				ProductDatabase.insertProduct(product);
				rb = Response.ok(product.getProperties());
			}		
		}
		else
		{
			rb = Response.serverError().status(403);
		}
		
		return rb.build();
	}

	@RolesAllowed({"ADMIN"})
	@PUT
	@Path("/products/{id_product}/{id_videogame}/{id_console}/{price}/{date}")
	public Response updateProduct(@PathParam("id_product") int id_product, 
			@PathParam("id_videogame") int id_videogame,
			@PathParam("id_console") int id_console,
			@PathParam("price") int price,
			@PathParam("date") String date,
			@Context HttpRequest request)
	{
		Product product = ProductDatabase.findProductByID(id_product);

		ResponseBuilder rb;
		if(product == null)
		{
			rb = Response.serverError().status(404);
		}
		else
		{
			
			Console console = ProductDatabase.findConsoleByID(id_console);
			VideoGame videogame = ProductDatabase.findVideoGameByID(id_videogame);
			if(console != null && videogame != null)
			{
				product.setConsole(console);
				product.setDate_release(date);
				product.setPrice(price);
				product.setVideogame(videogame);

				ProductDatabase.insertProduct(product);				
				rb = Response.ok(product.getProperties());
			}
			else
			{
				rb = Response.serverError().status(404);
			}

		}		
		return rb.build();
	}

	@PermitAll
	@GET
	@Path("/products/{id}/sameProducts")
	public Response getSameTagProducts(@PathParam("id") int id, @Context HttpRequest request)
	{
		Product product = ProductDatabase.findProductByID(id);
		ResponseBuilder rb;
		if(product == null)
		{
			rb = Response.serverError().status(404);
		}
		else
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String json = "[]";
			try {
				json = mapper.writeValueAsString(ProductDatabase.findAllSameProducts(product));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			rb = Response.ok(json);
		}			
		return rb.build();
	}




	@PermitAll
	@GET
	@Path("/products/{id_product}/reviews")
	public Response getReviews(@PathParam("id_product") int id_product, @Context HttpRequest request)
	{
		Product product = ProductDatabase.findProductByID(id_product);
		ResponseBuilder rb;
		if(product == null)
		{
			rb = Response.serverError().status(404);
		}
		else
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String json = "[]";
			try {
				json = mapper.writeValueAsString(product.getReviews());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			rb = Response.ok(json);
		}		
		return rb.build();

	}

	@PermitAll
	@GET
	@Path("/products")
	public Response getProducts(@Context HttpRequest request)
	{
		List list = ProductDatabase.findAll();
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String json = "[]";
		try {
			json = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		ResponseBuilder rb;
		if(json.isEmpty())
		{
			rb = Response.serverError().status(404);
		}
		else
		{
			rb = Response.ok(json).status(200);
		}		
		return rb.build();
	}


	@RolesAllowed({"ADMIN"})
	@POST
	@Path("/publishers/{name}")
	public Response createPublisher(@PathParam("name") String name, @Context HttpRequest request)
	{
		/** Prevent XSS **/

		Publisher publisher = ProductDatabase.findPublisherByName(name);
		if(publisher == null){
			publisher = new Publisher();
			publisher.setName(name);

			ProductDatabase.insertPublisher(publisher);

			publisher = ProductDatabase.findPublisherByName(name);
			return Response.ok(publisher.getProperties())
					.status(200)
					.build();
		}
		else
		{
			System.err.println(" post publisher 500 ");
			return Response.ok().status(500).build();
		}
	}


	@RolesAllowed({"ADMIN"})
	@POST
	@Path("/publishers/{name}")
	public Response updatePublisher(@PathParam("id_publisher") int id_publisher, @PathParam("name") String name, @Context HttpRequest request)
	{
		System.err.println(" post publisher ");

		Publisher publisher = ProductDatabase.findPublisherByID(id_publisher);

		if(publisher != null){

			publisher.setName(name);
			ProductDatabase.insertPublisher(publisher);

			return Response.ok(publisher.getProperties())
					.status(200)
					.build();
		}
		else
		{
			return Response.ok().status(404).build();
		}
	}

	@RolesAllowed({"ADMIN"})
	@POST
	@Path("/genres/{name}")
	public Response createGenre(@PathParam("name") String name, @Context HttpRequest request)
	{
		Genre genre = ProductDatabase.findGenreByName(name);
		if(genre == null){
			genre = new Genre();
			genre.setName(name);

			ProductDatabase.insertGenre(genre);

			genre = ProductDatabase.findGenreByName(name);
			return Response.ok(genre.getProperties())
					.status(200)
					.build();
		}
		else
		{
			return Response.ok().status(500).build();
		}
	}


	@RolesAllowed({"ADMIN"})
	@PUT
	@Path("/genres/{id_genre}/{name}")
	public Response updateGenre(@PathParam("id_genre") int id_genre, @PathParam("name") String name, @Context HttpRequest request)
	{
		Genre genre = ProductDatabase.findGenreByID(id_genre);
		if(genre != null){

			genre.setName(name);
			ProductDatabase.insertGenre(genre);

			return Response.ok(genre.getProperties())
					.status(200)
					.build();
		}
		else
		{
			return Response.ok().status(404).build();
		}
	}


	@RolesAllowed({"ADMIN"})
	@POST
	@Path("/consoles/{name}")
	public Response createConsole(@PathParam("name") String name, @Context HttpRequest request)
	{
		Console console = ProductDatabase.findConsoleByName(name);
		if(console == null){
			console = new Console();
			console.setName(name);

			ProductDatabase.insertConsole(console);

			console = ProductDatabase.findConsoleByName(name);
			return Response.ok(console.getProperties())
					.status(200)
					.build();
		}
		else
		{
			return Response.ok().status(500).build();
		}
	}


	@RolesAllowed({"ADMIN"})
	@PUT
	@Path("/consoles/{id_console}/{name}")
	public Response updateConsole(@PathParam("id_console") int id_console, 
			@PathParam("name") String name,
			@Context HttpRequest request)
	{
		Console console = 	ProductDatabase.findConsoleByID(id_console);
		if(console != null){

			console.setName(name);
			ProductDatabase.insertConsole(console);

			return Response.ok(console.getProperties())
					.status(200)
					.build();
		}
		else
		{
			return Response.ok().status(404).build();
		}
	}
}