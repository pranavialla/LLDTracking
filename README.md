You need to build a simplified advertisement engine.

Problem Statement
Design and implement an advertisement engine. The system manages Advertisers, Ads, and User Ad requests. It must select and serve the most relevant Ad based on matching criteria, bidding amount, and system constraints while respecting budget constraints.

Different terminology:

Advertiser: An entity that publishes advertisements on the platform. Advertisers also keep their advertisement budget on the platform.
User: An entity that uses the platform and consumes advertisements with other use cases on the platform.
Bid Amount: Bid amount is the amount earned per advertisement by the platform from the advertiser. If the Bid amount is higher, then there are more chances of the advertisement getting served to the customer.
Advertisement Campaign: The Advertiser creates advertisement campaigns on the platform. The advertiser provides info such as URL, content_type (image, video), and bid per Ad. matching, and targeting attributes such as age, interest, and gender, etc., with the campaign.
System Constraints: The Platform will have some constraints and will be checked against all the advertisements before matching.
Advertisement Matching: The platform provides advertisements, comparing user details and attributes with all the active campaign requirements. It also compares the bid amount with the existing budget and checks for all the given system constraints for advertisement serving to find the best-matching advertisement. Once an advertisement is served, the budget is reduced from the advertiser’s account. An advertisement is served only when the bid amount is less than the existing budget.

Requirements P0
Provide interfaces to add advertisers and their budgets.
add_advertiser(name)
add_budget(budget)
Provide interfaces to add users and their attributes to the system, such as date of birth, interests, gender, etc.
add_user(user, dob, gender)
add_attribbute(attributes)
Provide an interface to create an advertisement campaign for the advertiser.
create_campaign(advertiser_id, bid_amount, url, type, age, city, constraints)
Provide an interface to request best best-matching advertisement from the system. This should return a single advertisement per API call.
match_advertisement(user_id, city)
All the above api syntaxes are symbolic. Please design and write the method names, method parameters as per the best coding practices.

System Constraints:

A user shouldn’t see the same advertisement if he has seen it in the last 10 fetch instances.
At the global level, don’t serve the same advertisement if it has been served 5 times in the last 1 minute.
match_advertisement can return null if all the advertisements after the criteria matching fail the system constraints validation for a user.

Requirements P1
Handle concurrency cases for different scenarios.
Handling if more system constraints keep getting added to fetch advertisements.

Things to keep in mind
You are only allowed to use in-memory data structures
You are NOT allowed to use any databases
You are NOT required to have a full-fledged web service or APIs exposed
You are required to showcase the working of the above concept.
Just a main class that simulates the above operations is enough. Provide some valid test cases as well.
Should you have any doubts, you are allowed to make appropriate assumptions, as long as you can explain them during the evaluation.
You are allowed to code on your favorite IDEs as long as you paste the code back into the tool within the allotted time frame
How you will be evaluated
You are expected to write production-quality code while implementing the requirements.
We look for the following:

Separation of concerns
Abstractions
Application of OO design principles
Testability
Code readability
Language proficiency
[execution time limit] 4 seconds (sh)

[memory limit] 2g