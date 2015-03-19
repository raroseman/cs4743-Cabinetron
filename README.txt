CS 4743 assignment 4
Josef Klein and Ryan Roseman

Requirements implementation:

Note to TA: We talked to Professor Robinson about the graph for merging 
GUI_Implementation. He told us that the issue was from a fast-forward merge 
and he could tell that the branch was created and developed on. 
He instructed us to let you know not to penalize the git branch development
requirement based on this issue since reversing the merge would cause problems.
Please speak with him regarding any questions. Thank you, Ryan and Joe.

Assignment 4 requirements implementation:

Model - Product Template:
Implemented as the ProductTemplate.class file. Is a single instance
of a Product Template. Meets the constraints described (automatically
generated unique ID is handled by the database table 'ProductTemplate').
Product # max length of 20 is enforced, as is uniqueness and starting with 'A'.
Product description max length of 255 is enforced.

Model - Product Templates:
Implemented as the ProductTemplateModel.class file. Contains a list of
all ProductTemplate objects.

Model - Product Template Part:
Implemented as the ProductTemplatePart.class file. Is a single instance
of a Product Template Part. The database meets the requirements
(template ID is a foreign key, as is part ID, and these form a unique index).
Quantity greater than 0 is enforced in the class.

Model - Product Template Parts:
Implemented as the ProductTemplatePartsModel.class file. Contains a list
of ProductTemplateParts.

User Interfaces - Product Template List View:
Implemented as the ProductTemplateListView.class file.

User Interfaces - Product Template Detail View:
Implemented as the ProductTemplateDetailView.class file.

User Interfaces - Product Template Parts List View:
Implemented as the ProductTemplatePartView.class file.

User Interfaces - Product Template Parts Detail View:
Implemented as the ProductTemplatePartDetailView.class file.

Behavior - Product Templates:
Add/View/Delete Product Template is handled by the ProductTemplateView class.
Edit Product Template is handled by the ProductTemplateDetailView class.
View Product Template Parts is handled by the ProductTemplateView class.
Add/Delete/View Product Template Part is handled by ProductTemplatePartView.
Edit Product Template Part is handled by the ProductTemplatePartDetailView.
All constraints are enforced (by the instance or Gateways where appropriate).

Change requests:
1. Modified the Part class to enforce the Part Number starting with a 'P'.
2. Added a check in the PartsInventoryGateway to check for association of a
Part with a Product Template before deletion.

Bug fixes:
Used optimistic locking in the ItemView; based on changes to the Timestamp
in the InventoryItem table in the database. When the client opens an 
InventoryItem for view/edit, the timestamp is acquired. If the user edits the
part, upon submitting their changes, if the database contains a newer timestamp
for that InventoryItem, the user will see the conflict in the ItemView window
along with the difference in InventoryItem details.

Datasource:
There are two new Gateways and two new tables. 
The ProductTemplateGateway and the ProductTemplatePartGateway. In the database,
there is the ProductTemplates table and the ProductTemplateParts table.


