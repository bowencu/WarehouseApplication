# Marketplace Store
### CPSC-210 Project by Bowen Cui

## Description
This application aims to produce similar functionality as an online shopping marketplace. The features provided aim 
to fit both seller's and buyer's needs alike. Products that are uploaded can include various types and features that a seller may wish to advertise. A buyer has options to browse 
products according to their needs, such as filtering for a specific category or sorting products according to price. 
When a purchase is made, the price paid by the buyer goes into the seller's account balance. This idea was of interest 
to me because 
shopping can be a frustrating experience for 
customers, especially 
as online marketplaces are increasingly common. The process of finding the right product can be made easy for the 
customer by implementing ways for one to express their criteria through their browsing experience.

**As a user, I want to be able to:**
- sell products by uploading products and their attributes to the warehouse inventory
- earn the amount paid by a buyer as a seller
- include details on a products for sale *(name, price, sale, category, used condition, description, stock left, etc.)*
- view all products currently for sale
- filter products according to their features *(name, price, sale, category, used condition, description, etc.)*
- buy products and add them to my personal inventory
- have an account that stores my remaining balance for purchases on this application
- apply discounts to all currently listed products during promotional events, or only to products that fit a specified 
  criteria
- manage stock for each product in a warehouse
- save the entire state of the warehouse inventory and individual shopper information (balances, inventories)
- reload a previously saved state of warehouse to continue shopping with the same inventory and shopper information

## Instructions for Grader

- You can make purchases by removing products from the warehouse inventory and adding them to your personal inventory
- You can filter products seen in the warehouse inventory by price range, discounted items, used items, and category
- You can locate my visual component (image of a lighthouse) on the login window
- You can save the state of my application by clicking the "save" button on the login window
- You can reload the state of my application by clicking the "open" button on the login window

## Example of Logged Events (with date and time)

Welcome Store Owner!

UBC Sweater added to Warehouse Inventory for $55.0

Toaster added to Warehouse Inventory for $44.99

MacBook Pro added to Warehouse Inventory for $2500.0

MacBook Pro is on sale for $2250.0.

Welcome back Bowen!

Bowen has loaded $1000.0 to balance.

Store Owner has loaded $44.99 to balance.

Bowen bought Toaster for $44.99.

Store Owner has loaded $55.0 to balance.

Bowen bought UBC Sweater for $55.0.

Toaster is selling for $20.0.

Bowen is selling Toaster


## Phase 4: Task 3 Refactoring Ideas

In the UML diagram, every association arrow that points to the Warehouse class has a 1 multiplicity of it. This 
makes sense since there's only ever one Warehouse object when running the program. The singleton design pattern 
would be well suited for this purpose so that any reference to a warehouse object would be the one and only 
warehouse instance in the entire program. Secondly, each of the Market, StoreManager, and User classes represent a 
window that could open when running WarehouseGUI. However, their association arrows are exactly the same as the all 
have two associations of multiplicity 1 to the Warehouse and Person class. Since they are used for WarehouseGUI 
purposes, a subtyping relationship could simplify association needed, by making WarehouseGUI the supertype, and 
Market, StoreManager, and User subtypes of it. Also, there will likely be duplicated methods between the classes 
that can be reduced since they all concern a similar usage concerning the graphical user interface.


Code in the model and test persistence packages:
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

Code in Event and EventLog classes:
https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git