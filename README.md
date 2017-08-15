# M.S. Thesis

This is a base for my M.S. thesis "Comparative analysis of Java 8 Stream API vs in-memory SQL". It mainly consists of JMH benchmarks, comparing raw data processing performance of three data-retrieval technologies: H2, MySQL and Java 8 Stream API. In brief, no definite results were obtained - each data access method excels at different use cases and it is basically impossible to make any general assumption (not surprising). However, I am really satisfied with this project, because anyway I learned a lot of new things and I was able to test my knowledge against strict academic staff :)

**The code is horrible!** You have been warned. It served it's purpose, and I'm done with it. It was very hard to make a decent object model of my tests (while working around some arcane JMH project structure requirements), and I think I did my best, however, there is still a lot of duplication and similar cruft. I'm not proud of the code, rather of the work I put to get this piece of paper.

All different pieces:
* TPC dataset generator integration with automatic database seeding
* two sets of JMH benchmarks - sample generated data and strict TPC data
* some unit tests, because this whole thing is way too brittle to base any conclusions on without tests
* huge automation layer and plot generation scripts
* actual text of my thesis

Some buzzwords aka TLDR:
* Java 8 + Gradle + JMH
* TPC + SQL + H2
* Python + matplotlib




