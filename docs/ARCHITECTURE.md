# 🏗️ System Architecture

## Authentication & Security

The system secures endpoints using **JSON Web Tokens (JWT)**.

- **Login/Registration:** Users receive a short-lived access token.
- **Logout (Invalidation):** Instead of just deleting the token on the client side, the server adds the token signature to a **Redis Blacklist** until it expires, preventing stolen tokens from being reused.

## Scheduling Engine: Working Schedule ➔ Slot Generator

The core complexity of MediCareFlow lies in its automated slot generation:

1. **Working Schedule:** Admins or Doctors define recurring weekly schedules (e.g., "Mondays, 09:00 - 15:00").
2. **Consultation Type:** Defines the duration of an appointment (e.g., "General Checkup - 30 mins").
3. **Slot Generator:** A background service (or triggered API) reads the schedules and divides the continuous working hours into discrete, bookable `TimeSlot` entities based on the consultation duration.

## Redis Cache Usage

Redis is utilized for two primary functions:

1. **Token Blacklisting:** Tracking invalidated JWTs during user logout.
2. **Performance Optimization:** Caching frequently accessed, infrequently changed data (like Consultation Types or public schedules) to reduce MySQL database load.
