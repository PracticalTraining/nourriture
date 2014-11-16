#nourriture

## Contents
* [Background](#背景介绍)
* [Introduction](#项目介绍)
* [Instruction](#说明)
  * [Access Code](#获取代码)
  * [Data Modeling](#数据建模)
  * [Framework](#开发框架)
  * [Interface](#接口)
       * [introduction to interface](#接口介绍)
  * [Restuful web services](#Restuful服务)
* [Others](#其他)
	*  [Maven](#Maven)

<a name="Background"></a>
## Background
With Nourriture, innovative food suppliers and app developers have the most comprehensive platform from which they can quickly and accurately create relevant tools for consumers to make informed decisions about food.

### Transparency ###
There is growing demand to access reliable information about product composition for both health and ethical reasons. There are, however, no tools available to supply this information.
### Health ###
It is estimated that about 60% of people around the world could suffer from food intolerance or food related inflammation.
### Ethics and Religion ###
Many others follow religious or weight based diets and people are increasingly becoming aware that the way we eat is one of the primary sources of many health problems and at the same time the primary tool for well-being.

<a name="Introduction"></a>
## Introduction

Nourriture is a platform for ingredients, which enables the development of applications that help people make educated and engaged choices about what they consume

Nourriture is implemented by website and android app, which makes mobility possible.

We have finished the interface of Restful services, now we are going to implement the front end , both website and android app.

<a name="Instruction"></a>
## Instruction

<a name="Access Code"></a>
### Access Code

* git for Nourriture: <https://github.com/PracticalTraining/nourriture>
* continuing updating

<a name="Data Modeling"></a>
### Data Modeling


1. Conceptual data modeling:
	Entity:Customer,manufacturer,recipe,food,admin,comment,category,location
![](http://i.imgur.com/xgRZjdX.png)
2. Logical data modeling
![](http://i.imgur.com/5CqAR3K.png)
3. Physical data modeling:database, table, fields in mysql

<a name="Framework"></a>
### Framework

In this project we use SSH(struts+spring+hibernate),which is a integrated opensource framework

Four layers:1.Presentation 2.Business Logic 3.Data Persistence 4.Domain

Specific steps:</br>
Struts is responsible for Infrastructure of the system, separate MVC,in the module of Struts it controls the redirect of the business, use Hibernate framework to support  Persistence, Spring manages Struts and Hibernate.

Use OO(Object Oriented) to propose model according to requirements, then implement these models by simple java object, and then create the basic DAO(Data Access Objects) interface, and implement these DAOs in Hibernate. We use Hibernate framework to realize the transfer and visit between Java class and database. In the final use Spring to manages  Struts and Hibernate.

Basic Business Logic: In Presentation layer, firstly by JSP webpage to realise the interact interface, responsible for Request and Response, and then Struts will assign the Request to the corresponding Action baesd on configure document(struts-config.xml).In Business Logic layer,
Sprint IoC is reponsible for providing Model the Action and the assisted data handling component(DAO) to complete the Business Logic, and at the same time it will provide transaction handling ... container to improve the performance and ensure the integrity of the system. In Data Persistence layer, rely on Object Relational Mapping and interaction with database of Hibernate to handle the Request of DAO component, in the final return the results.

<a name="Interface"></a>
### Interface
    interface for DAO
	`Customer example`
    public interface ICustomerDao {
	/** add one row **/
	int add(Customer customer);
	/** check if name is already exist **/
	boolean isNameExist(String name);
	/** search the customer according to name **/
	List<Customer> searchByName(String name);
	/** check if there exsit a row with given name and password
	 *  @return -1 if not exsit
	 *  @return customer id if exsit
	 */
	int login(String name,String password);
	/** select a row by id **/
	Customer getById(int id);
	/** update customer **/
	void update(Customer customer);}

<a name="Restuful web services"></a>
### Restuful web services

In the project we use Jersey to implement the service.

JAX-RS provides some annotations to aid in mapping a resource class (a POJO) as a web resource. The annotations include:

@Path specifies the relative path for a resource class or method.</br>
@GET, @PUT, @POST, @DELETE and @HEAD specify the HTTP request type of a resource.</br>
@QueryParam binds the method parameter to the value of an HTTP query parameter.</br>
@DefaultValue specifies a default value for the above bindings when the key is not found.

For information we use Json(JavaScript Object Notation)

         {sdesc":"test desc","userid":"testuserid","uri": 
       "http://java.sun.com","ldesc":"long test description"} 

Sample for Hello World:

    @Path("/hello")
    public class HelloResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
    return "Hello Jersey";
    }
    }

Visit by this url:

    http://host:port/services/hello

More example:

    @Path("/services")
    public class Resources {
     
    @Path("/user")
    public UserResource getUserResource() {
    ...
    }
     
    @Path("/book")
    public BookResource getBookResource() {
    ...
    }
    }
    UserReso


<a name="Others"></a>
## Others

###Maven

    <!--jersey-->
    <dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-servlet-core</artifactId>
    <version>2.0</version>
    </dependency>
     
    <!--JAXB API-->
    <dependency>
    <groupId>javax.xml.ws</groupId>
    <artifactId>jaxws-api</artifactId>
    <version>2.1</version>
    </dependency>
     
    <!-- Json支持 -->
    <dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-core-asl</artifactId>
    <version>1.9.12</version>
    </dependency>
    <dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-mapper-asl</artifactId>
    <version>1.9.12</version>
    </dependency>
    <dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-jaxrs</artifactId>
    <version>1.9.12</version>
    </dependency>


