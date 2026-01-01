# Team Project: Phish Detect

## Summary

Phish Detect is an application that allows users to submit emails to analyze for any possible phishing attempts. After analyzation, the user can pin an email to the dashboard where other users can view and filter emails. UofT IT admin can log in and view the email's content and mark the email as confirmed phishing, safe, or pending.

## User Stories and Use Cases

### Use Case 1: Submit Emails for Analysis
The user goes to the Submit Email window, pastes an email's sender, subject line (title), and the body. The user submits the email for analyzation, and the LLM will analyze the email using a fixed prompt, and generate a suspicion score, an explanation of that score, and what to do next. The user has the option to pin the email to the dashboard if they would like.

### Use Case 2: Joining the Discord Server and Receiving Updates
The user goes to the dashboard and clicks the Join Discord button that opens the Phish Detect server's invite link. The user joins the server, and will be pinged when new information arises (such as new phishing emails to look out for and new features).

### Use Case 3: Filtering and Viewing Pinned Emails on the Dashboard
The user goes to the dashboard, enters any keywords and senders they would like to search for, a minimum suspicion score and/or maximum suspicion score that each filtered email should have, and a value to sort by (such as Title, Sender, Suspicion Score, and Date). The user clicks the Apply Filter button and the new pinned emails will show up. 
- If no pinned emails match the filter criteria, a pop-up will appear saying "No emails matched"
- If the min and max score cannot be parsed, a pop-up will appear saying "Invalid score format"

### Use Case 4: IT Sign-In and IT Dashboard
The user (a UOfT IT employee) signs in through the IT login page using the correct username and password. The user is guided to the IT dashboard, which has the same look and feel as the normal dashboard, except the user can click on an email and view its contents and the explanation. The user can mark the email as confirmed to be phishing, safe, or pending (to look at later). The user can go back to the IT dashboard by clicking the back button to view or check more emails.

### Use Case 5: Viewing Output Explanation
After the user submits an email, an explanation is generated on why the email is suspicious/not suspicious. This explanation is saved to the database, along with the email, and can be viewed in the IT dashboard.

### Use Case 6: Viewing Link Risk Analysis
After the user submits an email, any links given within the email's body will be given in the explanation box, and, using an external API, is deemed either to be dangerous or safe. This allows the user to cross-reference with the generated explanation and suspicion score to decide what to do next. 

