# Transaction Management Example

I made this spring boot application that help transaction management, and its need in spring boot project.

Clone it from my github repository by clicking [this link](https://github.com/aamirshayanshaikh/spring-transaction-management).

#### > Prerequisites

* Java 17
* IDE (Intellej IDEA)
* MySql
* Maven 3.0
* Git
* Postman.

# Introduction

I think it's imperative that everone working with data understand transactions.

In this post i'll explain what is a transaction is ? the 4 ACID key propetrties, we'll also discover different types and strategies of a transaction.

I'll dive deeper into the declarative and programatic trancs management and the patterns/paradigms spring uses behind the scenes to implement them.

Spring transaction management features is one of the most widely used features of Spring framework, so what are pros and when to opt for programmatic management over declarative management and vice versa.
# What is a transaction ?
Transaction is a series of actions that fail as a group or complete entirely as a group, all these actions should be rollback in case of any failure, but if all of them complete then the transaction should be permanently comitted.

## Transaction Initialization
Despite creating controller I have initialized transaction in main class of the application.

````
@SpringBootApplication
public class TransactionManagementExampleApplication {

    private final InsertFirstAuthorService insertFirstAuthorService;
    public TransactionManagementExampleApplication(InsertFirstAuthorService insertFirstAuthorService) {
        this.insertFirstAuthorService = insertFirstAuthorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TransactionManagementExampleApplication.class, args);
    }
    
    @Bean
    public ApplicationRunner init() {
        return args -> {
            System.out.println("=========== Initialing Transaction =================================");
            insertFirstAuthorService.insertFirstAuthor();
            System.out.println("============================================");

        };
    }
}

````
## Transaction Handling
We are handling our transaction using @Transaction annotation in InsertSecondAuthorService class.


````
@Service
public class InsertSecondAuthorService {

    private final AuthorRepository authorRepository;

    public InsertSecondAuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void insertSecondAuthor() {

        Author author = new Author();
        author.setName("Alicia Tom");
        authorRepository.save(author);
        
        if(new Random().nextBoolean()) {
            throw new RuntimeException("DummyException: this should cause rollback of both inserts!");
        }
    }
}
````

## Transaction Propagation
Transaction Propagation indicates if any component or service will or will not participate in transaction, and how will it behave if the calling component or service already has or does not have a transaction created already.

#### PROPAGATION_REQUIRED
If DataSourceTransactionObject T1 is already started for Method M1. If for another Method M2 Transaction object is required, no new Transaction object is created. Same object T1 is used for M2.

#### PROPAGATION_MANDATORY
Method must run within a transaction. If no existing transaction is in progress, an exception will be thrown.

#### PROPAGATION_REQUIRES_NEW
If DataSourceTransactionObject T1 is already started for Method M1 and it is in progress (executing method M1). If another method M2 start executing then T1 is suspended for the duration of method M2 with new DataSourceTransactionObject T2 for M2. M2 run within its own transaction context.

#### PROPAGATION_NOT_SUPPORTED
If DataSourceTransactionObject T1 is already started for Method M1. If another method M2 is run concurrently. Then M2 should not run within transaction context. T1 is suspended till M2 is finished.

#### PROPAGATION_NEVER
None of the methods run in transaction context.
#### PROPAGATION_NESTED
The NESTED behavior makes nested Spring transactions to use the same physical transaction but sets save points between nested invocations so inner transactions may also rollback independently of outer transactions.




## Isolation level
defines how the changes made to some data repository by one transaction affect other simultaneous concurrent transactions, and also how and when that changed data becomes available to other transactions. When we define a transaction using the Spring framework we are also able to configure in which isolation level that same transaction will be executed.
````
@Transactional(isolation=Isolation.READ_COMMITTED)
public void someTransactionalMethod(Object obj) {

}
````

#### READ_UNCOMMITTED
Isolation level states that a transaction may read data that is still uncommitted by other transactions.

#### READ_COMMITTED
Isolation level states that a transaction can't read data that is not yet committed by other transactions.

#### REPEATABLE_READ
Isolation level states that if a transaction reads one record from the database multiple times the result of all those reading operations must always be the same.

#### SERIALIZABLE
Isolation level is the most restrictive of all isolation levels. Transactions are executed with locking at all levels (read, range and write locking) so they appear as if they were executed in a serialized way.

However, propagation is the ability to decide how the business methods should be encapsulated in both logical or physical transactions.

# Transaction types

Depending on the enviroment where you're managing the transaction, there are two types of transactions:

### 1. Global transaction

They're used when multiple resources manage trx, can span multiple transactional resources i.e an application server allowing for access to many relational databases and message queues.

Global transaction management is required in a distributed computing environment where all the resources are distributed across multiple systems. In such a case, transaction management needs to be done both at local and global levels. A distributed or a global transaction is executed across multiple systems, and its execution requires coordination between the global transaction management system and all the local data managers of all the involved systems.

### 2. Local transaction

Local transaction management can be useful in a centralized computing environment where application components and resources are located at a single site, and transaction management only involves a local data manager running on a single machine. Local transactions are easier to be implemented that's why they's our case in the TransferMoney Spring Boot application.

---

# 4 Key ACID properties

All transactions processing systems must implement:

- Failure recovery (Atomicity, Durability).
- Concurrency control (Isolation, Consistency).

The ACID properties, in totality, provide a mechanism to ensure correctness and consistency of a database in a way such that each transaction is a group of operations that acts a single unit, produces consistent results, acts in isolation from other operations and updates that it makes are durably stored.


### > Atomicity

By this, we mean that either the entire transaction takes place at once or doesn’t happen at all. There is no midway i.e. transactions do not occur partially.
Each transaction is considered as one unit and either runs to completion or is not executed at all.

### > Consistency

This means that integrity constraints must be maintained so that the database is consistent before and after the transaction.
It refers to the correctness of a database.


### > Isolation

This property ensures that multiple transactions can occur concurrently without leading to the inconsistency of database state.

Transactions occur independently without interference. Changes occurring in a particular transaction will not be visible to any other transaction until that particular change in that transaction is written to memory or has been committed.

Isolation mailnly help avoid those kind of problems:

-     Dirty reads
-     Non-repeatable reads
-     Phantom reads (lecture fontôme)

Read more about that [here](<https://en.wikipedia.org/wiki/Isolation_(database_systems)#Read_phenomena>).

### > Durability

This property ensures that once the transaction has completed execution, the updates and modifications to the database are stored in and written to disk and they persist even if a system failure occurs.
These updates now become permanent and are stored in non-volatile memory, so the effects of the transaction, thus, are never lost.

---

# Why using Spring Framework for transaction management ?

When it comes to trans, spring provides a lot of advantages:

- Providing a consistent programming model across global and local transactions (by setting a uniform API across all different transaction & persistence APIs.)
- lightweight and flexible trans. management.
- Support for both programmatic & declarative trans. management.
- Extra support of springboot to transaction management.
- Benefits from different trans. management strategies.

if this looks too complicated to you don't be discouraged, we'll explain this thourghly.

What I mean by a **_consistent programming model_** is that Spring set a uniform API across all different transaction & persistence APIs, because obviously there is several different APIs involved on manage trans such as:


# Transaction management types

Transaction management ensures data consistency and integrity,

As i mentioned before, spring supports both programmatic & declarative trans. management, and these are the two ways you handle and manage transactions:

#### > Declarative transaction management:

An approach that allows the developer separates transaction management from business code, you start by setting your configuration XML based that uses XML files outside of the code or via Java annotations.

- Manage transactions via configuration.
- Separate transaction logic from business logic.
- Easy to maintain.
- Preferred when a lot of transaction logic.

#### > Programmatic transaction management:

There is where developer writes custom code to manage the transaction and set boundaries.

- Explicitly coded transaction management.
- Manage transactions via code.
- Useful for minimal transaction logic.
- Flexible but difficult to maintain.
- Couples transaction and business logic.

#### > When to use declarative transaction management over programmatic trx management and vice versa ?

It depends on your needs, personally i'm usually using the annotation config for handling my transactions the declarative way in 90% of cases, however when sometimes you need more control to your transaction, i mean you want to decide when exactly commit your changes or whatever, then spring provide you support for programmatic management as well, so you can be more flexible with your transaction, what you should know is in that case you'll have to couple trans. technical code and your business logic and this make things harder for you to maintain.

---


# Implementing declarative transaction management

before starting the implemntation, let's list what spring does behind the scenes of your declarative trx management

> => Aspect Oriented Programming (AOP) paradigm & Proxy pattern

AOP is a programming paradigm that breaks programing logic into distinct parts, Spring uses this paradigm when implementing trx management which is enabled via proxies,

so when you're not using a proxy, here is what we have at runtime:

### Without proxy

The method is invoked directly on that object reference
![Image Alt](https://github.com/haffani/v4/blob/master/content/posts/spring-trx-management/no-proxy.png)

### With proxy

When a proxy is used and you invoke a method transferMoney on an object reference, the method is no longer invoked directly on that object reference, but instead on a reference to the proxy.

At startup time, a new class is created, called proxy. This one is in charge of adding Transactional behavior as follows:

The generated proxy class comes on top of AccountServiceImpl. It adds Transactional behavior to it.

So how to make sure that a proxy is indeed being used? For your own understanding, it’s interesting to go back into the code and see with your very eyes that you are indeed using a proxy.

A simple way is to print out the class name:

```java:title=java
AccountService accountService = (AccountService) applicationContext.getBean(AccountService.class);
String accountServiceClassName = accountService.getClass().getName();
logger.info(accountServiceClassName);
```

it should show an output similar to this:

    INFO : transaction.TransactionProxyTest - $Proxy13

This class is a dynamic Proxy, generated by Spring using the JDK Reflection API.

At shutdown (eg. When the application is stopped), the proxy class will be destroyed and you will only have AccountService and AccountServiceImpl on the file system:

---

> The default advice mode for processing `@Transactional` annotations is proxy.

To summarize proxy uses

- `Transaction interceptor` which intercepts method calls.
- `Platform transaction manager` that handles transactions.

At a high level, Spring creates proxies for all the classes annotated with @Transactional – either on the class or on any of the methods. The proxy allows the framework to inject transactional logic before and after the running method – mainly for starting and committing the transaction.

4 Components interacting with each other are used by spring to handle your trans.:

- **_`Persistence context proxy`_**

Cuz the entities live in database are managed by entity manager, it defines methods that are used to interact with persistence context.

- **_`Entity manager proxy`_**

database trans. happens inside the scope of a persistence context which is a set of managed entity instances that exist in a particular store (H2 in our case), it defines the scope for entity instances. (each enity manager is associated to a persistence context).

- **_`Transaction aspect`_**

intercept method calls with the annotation Transactional, what i should mention here is the interceptor is called before and after the method is invoked on the object reference (first call before to begin a transaction & second call after to commit changes), it has two main responsabilities, first determines if new trans needed, the other is determines decides when to commit, rollback or left running.

- **_`Transaction manager`_**

it's an abstraction abstraction, represented in our case by Spring's [platform transcation manager](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html) interface, we are using actually the JPA transaction manager that provide essential methods for controlling trans operations at runtime like begin, commit, rollback.

> Spring recommends that you only annotate concrete classes.

### Transaction settings

```java:title=Propagation
@Transactional ( propagation = Propagation.REQUIRED )
//Code will always run in a transaction

@Transactional ( propagation = Propagation.REQUIRES_NEW )
//Code will always run in a new transaction

@Transactional ( propagation = Propagation.NEVER )
//Method shouldn’t be run within a transaction
```

```java:title=Isolation
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
//Allows dirty reads
@Transactional(isolation = Isolation.READ_COMMITTED)
//Does not allow dirty reads
@Transactional(isolation = Isolation.REPEATABLE_READ)
//Result always the same if row read twice
@Transactional(isolation = Isolation.SERIALIZABLE)
//Performs all transactions in a sequence
```

```java:title=Timeout
@Transactional( timeout=5 )
//Timeout for the operation wrapped by the transaction
```

```java:title=ReadOnly
@Transactional( read-only = true )
/**
=> Transactions don’t write back to database
=> Optimizes data access
=> Provides a hint to the persistence provider
=> Only relevant inside a transaction
*/
```

Now here is an implementation of a delarative transaction management.

```java:title=DeclarativeTrxAccountService.java
/**
 * @author Hamza AFFANI
 * @version 1.0
 *          Declarative transaction management implementation
 */

@Service
 // highlight-next-line
@Transactional
@Qualifier("declarativeTrxManagementBean")
public class DeclarativeTrxAccountService implements IAccountService {

	private final NumberFormat fmt = NumberFormat.getCurrencyInstance();

	@Autowired
	private IAccountDAO accountDAO;

	@Override
	public List<Account> getAllAccounts() {
		return accountDAO.findAll();
	}

	@Override
	public void addAccount(Account account) {
		accountDAO.save(account);
	}

	@Override
	public Optional<Account> getAccount(int accountId) {
		return accountDAO.findById((long) accountId);
	}

 // highlight-start
	@Override
	public void transferMoney(Account from, Account to, double amount, double fee) {
		/**
		 * Transaction consists of two steps:
     * 1.) withdraw from the sender account an amount and set debited as last_operation
		 * 2.) deposit the same amount to the beneficiary's account and set credited as last_operation
		 */
	  withdraw(from, amount, fee);
		deposit(to, amount);
	}
   // highlight-end

	/**
	 * Deposits the specified amount into the account.
	 */
	private void deposit(Account to, double amount) {
		Account accountToCredit = getAccount(to.getId().intValue()).get();
      ...
			accountToCredit.setBalance(accountToCredit.getBalance() + amount);
			accountToCredit.setLast_operation("Credited");
		}
	}

	/**
	 * Withdraws the specified amount from the account.
	 */

	private void withdraw(Account from, double amount, double fee) {
		Account accountToDebit = getAccount(from.getId().intValue()).get();
      ...
			accountToDebit.setBalance(accountToDebit.getBalance() - amount);
			accountToDebit.setLast_operation("Debited");
		}
	}
}
```

---

# Implementing programmatic transaction management

### First implementing using `Platform transaction manager`

Handles transactions across Hibernate, JDBC, JPA, JMS, etc.

This implementation below shows also how to set the transction options as well such as propagation mode, isolation level, and timeout.

```java:title=ProgTrxManagerAccountService.java
/*
 * @version 1.0
 * This class represents the first implementation of the programmatic
 * transaction management using directly the transactionManager
 */

@Service
@Qualifier("progTrxManagerBean")
public class ProgTrxManagerAccountService implements IAccountService {

	private final NumberFormat fmt = NumberFormat.getCurrencyInstance();

	@Autowired
	private IAccountDAO accountDAO;

// highlight-next-line
	@Autowired
	// highlight-next-line
	private PlatformTransactionManager transactionManager;

	@Override
	public Optional<Account> getAccount(int accountId) {
		return accountDAO.findById((long) accountId);
	}

	@Override
	public void transferMoney(Account from, Account to, double amount, double fee) {
		/**
		 * Transaction consists of two steps:
     * 1.) withdraw from the sender account an amount and set debited as last_operation
		 * 2.) deposit the same amount to the beneficiary's account and set credited as last_operation
		 */
    // highlight-start
		TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

		try {
			withdraw(from, amount, fee);
			deposit(to, amount);
			transactionManager.commit(transactionStatus);
		} catch (RuntimeException e) {
			transactionManager.rollback(transactionStatus);
			throw e;
		}
    // highlight-end
	}
}
```

### Second implementing using `Transaction template`

Similar to Spring templates like JdbcTemplate and other available templates.

Transaction template API define the transaction boundary with the help of a callback method `TransactionCallbackWithoutResult` that doesn't return a result in this case, however it's possible that we have a callback method with a return type.

This implementation below shows also how to set the transction options as well such as propagation mode, isolation level, and timeout.

```java:title=ProgTrxTemplateAccountService.java
/**
 * @author Hamza AFFANI
 * @version 1.0
 *          This class represents the second implementation of the programmatic
 *          transaction management using spring transaction template
 */
@Service
@Qualifier("progTrxTemplateBean")
public class ProgTrxTemplateAccountService implements IAccountService {

	private final NumberFormat fmt = NumberFormat.getCurrencyInstance();

	@Autowired
	private IAccountDAO accountDAO;
 // highlight-next-line
	private final TransactionTemplate transactionTemplate;
 // highlight-start
	public ProgTrxTemplateAccountService(PlatformTransactionManager transactionManager) {
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
		this.transactionTemplate.setReadOnly(true);
 // highlight-end
	}

	@Override
	public Optional<Account> getAccount(int accountId) {
		return accountDAO.findById((long) accountId);
	}

	@Override
	public void transferMoney(Account from, Account to, double amount, double fee) {
		/**
		 * Transaction consists of two steps:
     * 1.) withdraw from the sender account an amount and set debited as last_operation
		 * 2.) deposit the same amount to the beneficiary's account and set credited as last_operation
		 */
      // highlight-start
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					withdraw(from, amount, fee);
					deposit(to, amount);
				} catch (NoSuchElementException exception) { // in case of no such account found
					exception.printStackTrace();
					status.setRollbackOnly();
				}
			}
		});
     // highlight-end
	}
}
```

