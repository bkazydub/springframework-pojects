This is a simple shop application created using Spring Framework, Hibernate and Thymeleaf.

Used Spring Framework modules are: spring-core, spring-webmvc, spring-data(-jpa), spring-security.

Application functionality:
It is a simple online shop application with 2 roles: admin and user (it's also possible to be a 'guest'). 
As admin you're capable of creating and editing new products.
As client (both registered and guest) you can add products to your cart, making checkout, check order status etc.
You can also make a new account.

The application can be locally started with:
  mvn tomcat7:run
Once a Tomcat server started you can access at:
  http://localhost:8080/shop/
