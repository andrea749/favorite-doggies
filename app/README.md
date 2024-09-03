## Instructions:
Open the app and start picking favorites by pressing the heart icon next to breed names. Any subbreeds are presented indented under the main breed. Then, use the bottom button to navigate to the images screen. 

## Tradeoffs:
In a production project, I would've gotten the image URLs in batches instead of all at once, especially since we might want more than one image per type. I'd do this by keeping track of what breeds the user hasn't seen yet, and make a request for URLs once the user hits the end of the LazyColumn and has seen all rendered pictures. I also would've included an authentication interceptor to Retrofit in case we needed to pass in authentication tokens to our requests. I'd also store images for favorites locally on the device so that the app isn't totally unusable when there's no network connection. I'd keep breed information and URLs in a database and cache the images associated with those URLs. 
I used regular DataStore, but if I wanted to make it easier to store more complex data in DataStore then I'd use a protocol buffer instead.

With UI design, I would've used tabs to navigate between screens instead of the button on the bottom. I also would've had those tabs scroll away as a user scrolls down. 
