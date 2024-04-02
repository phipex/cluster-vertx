from locust import task, between
from locust.contrib.fasthttp import FastHttpUser
from locust import events

limit = 100
cont = 0
class WebsiteUser(FastHttpUser):


    def increseCount(self):
        global cont
        cont += 1

    def isNotLimitCount(self):
        global cont
        global limit
        return cont < limit

    @task
    def search_all2(self):
        
        if self.isNotLimitCount():
            self.increseCount()
            self.client.get("/greeting", name ="01 /greeting")
        else:
            self.environment.runner.quit() 


