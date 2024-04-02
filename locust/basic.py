from locust import task, between
from locust.contrib.fasthttp import FastHttpUser


class WebsiteUser(FastHttpUser):

    # When a load test is started, an instance of a User class will be created for each simulated user.
    # Each user will start running within their own green thread.
    # When these users run they pick tasks that they execute, sleep for a while, and then pick a new task and so on.
    @task
    def search_all(self):
        self.client.get("/greeting")

    @task
    def search_all2(self):
        self.client.get("/greeting", name ="01 /greeting")
