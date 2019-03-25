Image Store application

Use Cases:

			- Create an application to store, retrieve, delete images 
			- Manage images into albums
			- UI page to shows store, retrieve, delete image/albums events
			- Data need to be available even after the service restarts
			- Optionally UI page to display images of album


Prerequisites

			- Minikube
			- Kubernetes
			- Docker
			- Kompose
			- Spring-boot
			- MySql
			- HTML, CSS, Javascript, ajax, jquery
			

Image store implementation details:

	This is a very tradition approach.The Image store application is created using production ready sprint-boot framework. For UI purpose I have used html,css, javascript, jquery and ajax. And I have used MySql database. Here I have created two tables, one for storing images and other for storing notifications. I have used Rest end points for CRUD operations. And data will be retained even after restart as I am using Persist Volume and Persist Volume Claims.
	I created docker-compose.yml file and then used Kompose(Kubernetes+Compose) to generate kubernetes deployable service and deploment yaml files.
	Therefore  we need to create following containers in the process of running application for the kubernetes platform.
			A Container for running the Spring Boot Application (developed application docker image)
			A Container for running the MySQL Server (mysql docker image)
				
				
				
Steps to deploy the application:

				- Clone the project into your system.
				- Create a docker container for MySQL.
						: MySQL Team has provided an official docker image of MySQL through Docker Hub.  (https://hub.docker.com/_/mysql/) . Therefore we can create the MysQL docker container easily.
				- Create docker container for Spring Boot Application.
						docker build -f Dockerfile -t spring-imagestore .(dont forget to add that . at the end)
				- Then use the following command to generate yaml files using the docker-compose.yml file.
						kompose convert -f docker-compose.yml
				- Start minikube.
				- Deploy all the generated yaml files using following command like below, which will create the Deployments, PODs and services.
						: kubectl create -f spring-compose-imagestore-container-service.yaml
						: kubectl create -f mysql-docker-container-deployment.yaml
						: kubectl create -f mysql-docker-container-claim0-persistentvolumeclaim.yaml
						: kubectl create -f spring-compose-imagestore-container-deployment.yaml
						: kubectl create -f spring-compose-imagestore-container-claim0-persistentvolumeclaim.yaml
				- Now open minikube dashboard and verify whether applications are up and running.
				- Chech for host IP and port(Usually it will be 192.168.99.100:8087 , at least for my application	). Application is now launched with GUI to do operations.


How to use application:

				- For uploading file(s), Click on the Choose files and and then choose some file and click submit.
				- For Retrieving the file, enter file name in the retrieve field and click submit. Image will be retrieved.
				- For deleting the file, enter file name inthe delete field and click submit. Image will be deleted.
				
				- After every operation takes place, a notification bar pops up and tells what operation has been performed.
				- And all the operation history is show in the adjacent table. Admin will see history of all the users and other users will see only their history.
				
