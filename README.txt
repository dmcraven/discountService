# discountService

A simple service which contacts JL non prod environment to return all products in a category. 
It discards any products which do not contain a was price, then formats the remaining values per the rules in the technical test. 

Noting that had this not been a technical test, I would have needed to return to the BA with queries: 
What to do with a price range?
What to do with a basicColor of "Multi"?
Instead, I have made a decision on how to handle each scenario and documented in the code. 

Testing has been done through a combination of JUnit and running on a simple Jetty server, though the compiled WAR should be able to run on any other server. 

To test on Jetty, download, import to eclipse and run on Jetty server using jetty:run configuration. Then access any of the following URLs
http://localhost:8080/webapi/jltest/
http://localhost:8080/webapi/jltest/ShowWasNow
http://localhost:8080/webapi/jltest/ShowWasThenNow
http://localhost:8080/webapi/jltest/ShowPercDscount

Which should return JSON responses shown below

http://localhost:8080/webapi/jltest/
http://localhost:8080/webapi/jltest/ShowWasNow
[{"productId":"3428696","title":"Hobbs Kiona Dress, Green","nowPrice":"£74","priceLabel":"Was £149, now £74"},{"productId":"3421340","title":"Phase Eight Beatrix Floral Printed Dress, Cream/Red","nowPrice":"£99","priceLabel":"Was £140, now £99"},{"productId":"3391561","title":"Phase Eight Katlyn Three Quarter Sleeve Spot Dress, Navy/Ivory","nowPrice":"£59","priceLabel":"Was £99, now £59"},{"productId":"3459039","title":"Hobbs Bayview Dress, Blue/Multi","nowPrice":"£55","priceLabel":"Was £89, now £55"},{"productId":"3467432","title":"Boden Rosamund Posy Stripe Dress","colorSwatches":[{"color":"Navy","rgbColor":"0000FF","skuid":"237334043"},{"color":"Mimosa Yellow","rgbColor":"FFFF00","skuid":"237334029"}],"nowPrice":"£63","priceLabel":"Was £90, now £63"},{"productId":"3341058","title":"Jolie Moi Bonded Lace Prom Dress","colorSwatches":[{"color":"Dark Grey","rgbColor":"808080","skuid":"237169362"},{"color":"Dark Mauve","rgbColor":"800080","skuid":"237169364"},{"color":"Dark Teal","rgbColor":"0000FF","skuid":"237169370"},{"color":"Berry","rgbColor":"800080","skuid":"237169205"},{"color":"Burgundy","rgbColor":"FF0000","skuid":"237169270"},{"color":"Navy","rgbColor":"0000FF","skuid":"237169572"},{"color":"Royal Blue","rgbColor":"0000FF","skuid":"237169620"}],"nowPrice":"From £59 to £68","priceLabel":"Was £85, now £68"}]

http://localhost:8080/webapi/jltest/ShowWasThenNow
[{"productId":"3428696","title":"Hobbs Kiona Dress, Green","nowPrice":"£74","priceLabel":"Was £149, then £89, now £74"},{"productId":"3421340","title":"Phase Eight Beatrix Floral Printed Dress, Cream/Red","nowPrice":"£99","priceLabel":"Was £140, now £99"},{"productId":"3391561","title":"Phase Eight Katlyn Three Quarter Sleeve Spot Dress, Navy/Ivory","nowPrice":"£59","priceLabel":"Was £99, then £63.75, now £59"},{"productId":"3459039","title":"Hobbs Bayview Dress, Blue/Multi","nowPrice":"£55","priceLabel":"Was £89, now £55"},{"productId":"3467432","title":"Boden Rosamund Posy Stripe Dress","colorSwatches":[{"color":"Navy","rgbColor":"0000FF","skuid":"237334043"},{"color":"Mimosa Yellow","rgbColor":"FFFF00","skuid":"237334029"}],"nowPrice":"£63","priceLabel":"Was £90, now £63"},{"productId":"3341058","title":"Jolie Moi Bonded Lace Prom Dress","colorSwatches":[{"color":"Dark Grey","rgbColor":"808080","skuid":"237169362"},{"color":"Dark Mauve","rgbColor":"800080","skuid":"237169364"},{"color":"Dark Teal","rgbColor":"0000FF","skuid":"237169370"},{"color":"Berry","rgbColor":"800080","skuid":"237169205"},{"color":"Burgundy","rgbColor":"FF0000","skuid":"237169270"},{"color":"Navy","rgbColor":"0000FF","skuid":"237169572"},{"color":"Royal Blue","rgbColor":"0000FF","skuid":"237169620"}],"nowPrice":"From £59 to £68","priceLabel":"Was £85, then £68, now £68"}]

http://localhost:8080/webapi/jltest/ShowPercDscount
[{"productId":"3428696","title":"Hobbs Kiona Dress, Green","nowPrice":"£74","priceLabel":"50% off - now £74"},{"productId":"3421340","title":"Phase Eight Beatrix Floral Printed Dress, Cream/Red","nowPrice":"£99","priceLabel":"29% off - now £99"},{"productId":"3391561","title":"Phase Eight Katlyn Three Quarter Sleeve Spot Dress, Navy/Ivory","nowPrice":"£59","priceLabel":"40% off - now £59"},{"productId":"3459039","title":"Hobbs Bayview Dress, Blue/Multi","nowPrice":"£55","priceLabel":"38% off - now £55"},{"productId":"3467432","title":"Boden Rosamund Posy Stripe Dress","colorSwatches":[{"color":"Navy","rgbColor":"0000FF","skuid":"237334043"},{"color":"Mimosa Yellow","rgbColor":"FFFF00","skuid":"237334029"}],"nowPrice":"£63","priceLabel":"30% off - now £63"},{"productId":"3341058","title":"Jolie Moi Bonded Lace Prom Dress","colorSwatches":[{"color":"Dark Grey","rgbColor":"808080","skuid":"237169362"},{"color":"Dark Mauve","rgbColor":"800080","skuid":"237169364"},{"color":"Dark Teal","rgbColor":"0000FF","skuid":"237169370"},{"color":"Berry","rgbColor":"800080","skuid":"237169205"},{"color":"Burgundy","rgbColor":"FF0000","skuid":"237169270"},{"color":"Navy","rgbColor":"0000FF","skuid":"237169572"},{"color":"Royal Blue","rgbColor":"0000FF","skuid":"237169620"}],"nowPrice":"From £59 to £68","priceLabel":"20% off - now £68"}]
