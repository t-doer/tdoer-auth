# T-Doer Auth Server

## Core Concepts

1. Product, Application and Client/Terminal

- A product consists of one or more applications and may have one or more clients/terminals (Browser, APP, WeChat, AppLet etc.);
- An application is a logically integrated tool with which user can finish something;
- A client is a product's terminal from which user can access the product;

2. Tenant, Product, User

- Tenant rents products to service its users;
- Tenant's users are divided into B-end users and C-end users;
- Products, to service B-end users or C-end users, are also divided into B-end products or C-end products;

3. Access Domain

- Each client of a product for each tenant has a unique domain
 
## Features

- Support multiple tenants
- Support B-end and C-end users synchronously
- Support Web SSO, either B-end product or C-end product

- Tenant-based multiple user details datasource, each datasource can have its own PasswordEncoder

  For that, I customize these:
 
  - Authentication and Authentication Provider
  - WebAuthenticationDetails
  - UserDetailsService
  - Token granters
  - AuthorizationServerTokenServices

- Check user account's status for each request

- Support multiple token creation policies: 

  - KICK_OFF_PREVIOUS
  - BLOCK_NEWLY_COMING
  - ALLOW_ALL

- Stateless, so support clustering deployment well

- Save authorization code and token to Redis

- Save client in Database

## TODO

1) Make token creation policy configurable in client's properties