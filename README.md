# AviateOps

AviateOps is an airline management system designed to streamline operations and improve efficiency within airline companies. The primary goal of AviateOps is to provide a comprehensive platform for managing flight schedules, staff assignments, maintenance tasks, and operational logistics.

This project was developed as part of an Object-Oriented Analysis and Design using Java course. During the development, various design principles and patterns were adhered to, such as the Single Responsibility Principle, Open/Closed Principle, the Strategy Pattern, and the MVC (Model-View-Controller) architecture.

## Features

- **Admin Dashboard**: 
  - Create and finalize flight schedules.
  - Provide relief to staff from scheduled duties.
  - Assign maintenance tasks to the maintenance crew.

- **Maintenance Crew**: 
  - Perform maintenance on aircraft.
  - Log maintenance reports.
  - View and manage maintenance tasks assigned by the admin.

- **Flight Staff and Pilots**: 
  - View flight schedules.
  - Accept assigned schedules.
  - Request relief from scheduled duties.

## Technology Stack

- **Frontend**: JavaFX
- **Backend**: Java

## Design Principles and Patterns

During the development of AviateOps, the following design principles and patterns were implemented:
- **Single Responsibility Principle**: Certain classes and modules in the system have one responsibility, making the system easier to maintain and extend.
- **Open/Closed Principle**: The system is designed to be open for extension but closed for modification, allowing new features to be added with minimal changes to existing code.
- **Strategy Pattern**: Used to define a family of algorithms, encapsulate each one, and make them interchangeable, enhancing flexibility and reusability.
- **MVC Architecture**: The application follows the MVC (Model-View-Controller) architecture, separating the application logic (Model), user interface (View), and control logic (Controller). This separation enhances modularity, making the system more manageable and scalable. It allows independent development, testing, and maintenance of each component, thereby improving the overall development process.

