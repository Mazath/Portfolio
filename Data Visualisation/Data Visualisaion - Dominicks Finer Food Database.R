#Creating Visualizations in relation to the Cheese data contained in the
#Dominick's Data set 
#(see https://www.chicagobooth.edu/research/kilts/research-data/dominicks)


#load libraries
library(tidyverse)
library(ggthemes)

#load data

cheese<- read.csv("cheese.csv")

cheese <- as_tibble(cheese)


#data Dictionary:

#store - Store Number
#UPC - Unique product number
#week - week of sale
#move -number of units sold
#qty - Number of items bundeled together
#price - Retail Price
#sale - Sale Code
#B - Bounus Buy
#S - Sale
#C - Coupon
#profit - gross margin
#OK - 1 for valid data 0 for trash


#remove trash data based on OK attribute
cheese <- filter(cheese, OK ==1)



#########################

head(cheese)

unique(cheese$STORE)
unique(cheese$UPC)
unique(cheese$SALE)
unique(cheese$QTY)

unique(cheese$PRICE)


meanPrice <- mean(cheese$PRICE)
meanPrice #2.28


storeList <- c(unique(cheese$STORE))


#Plot - line graph of profit by week

ggplot(data=cheese)+
  geom_line(mapping = aes(x=WEEK, y=PROFIT))+
  scale_x_continuous(name = "Week")+
  scale_y_continuous(name = "Profit")
  
#plot - line filterable by store
plotA<- ggplot(data=filter(cheese, STORE == 118))+
  geom_line(mapping = aes(x=WEEK, y=PROFIT))+
  scale_x_continuous(name = "Week")+
  scale_y_continuous(name = "Profit")+
  xlim(0,290)+
  ggtitle("Profit Trend of Store 118 Across Whole Dataset", subtitle = "Source: Dominick's Finer Food Database")+
  theme_economist()
  


#plot - line filterable by Product code
plot1<- ggplot(data=filter(cheese, UPC == 1600064380))+   #change UPC to select different item
  geom_line(mapping = aes(x=WEEK, y=PROFIT), color= "black")+
  scale_x_continuous(name = "Week")+
  scale_y_continuous(name = "PROFIT")+
  xlim(265,365)+                                          #change dependent on UPC trend line
  geom_hline(yintercept = mean(cheese$PROFIT) , color = "dark grey")+
  annotate("text", label = "Mean Product Profit", x=300, y=mean(cheese$PROFIT)+7, color = "gray56")+    #adjust depending on shape of line
  theme(panel.background = element_blank())+
  ggtitle("Profit Trend of Product UPC 1600064380", subtitle = "Source: Dominick's Finer Food Database")+
  theme_economist()




#plot2 <- line graph showing drop in product profit

plot2<- ggplot(data=filter(cheese, UPC == 1600064380))+   #change UPC to select different item
  geom_line(mapping = aes(x=WEEK, y=PROFIT), color= "black")+
  scale_x_continuous(name = "Week")+
  scale_y_continuous(name = "PROFIT")+
  xlim(282,312)+                                          #change dependent on UPC trend line
  geom_hline(yintercept = mean(cheese$PROFIT) , color = "dark grey")+
  annotate("text", label = "Mean Product Profit", x=300, y=mean(cheese$PROFIT)+7, color = "gray56")+    #adjust depending on shape of line
  theme(panel.background = element_blank())+
  ggtitle("Fall in Profit on Product UPC 1600064380", subtitle = "Source: Dominick's Finer Food Database")+
  theme_fivethirtyeight()





#plot 3 - Scatter plot of Number of units sold of product across range of weeks colored by store

plot3 <-ggplot(data = filter(cheese, UPC == 1600063840))+
  geom_point(mapping = aes(x=WEEK, y=MOVE, color = STORE))+  
  scale_x_continuous(name = "Week")+
  scale_y_continuous(name = "Number Sold")+
  geom_hline(yintercept = mean(cheese$MOVE) , color = "dark grey")+
  xlim(120, 185)+
  ggtitle("Count Of UPC 1600064340 Sold Across All Weeks", subtitle = "Source: Dominick's Finer Food Database")+
  theme_economist()



#remove nulls from SALE column
cheeseSaleNulls <- cheese
cheeseSaleNulls <- filter(cheeseSaleNulls, !SALE ==" ")
unique(cheeseSaleNulls$SALE)

#plot4 - plot of "L" "B" "S" "G" "C" by weeks 1,199,399
cheeseSalesWeek <- cheeseSaleNulls 
cheeseSalesWeek <- filter(cheeseSalesWeek, WEEK %in% c(1,199,399) )

plot4 <-ggplot(data = filter(cheeseSalesWeek, between(STORE, 110, 115 )))+
  geom_point(mapping = aes(x=WEEK, y=sum(MOVE), size = PROFIT))+  
  scale_x_continuous(name = "Week")+
  scale_y_continuous(name = "Number Sold")+
  geom_hline(yintercept = mean(cheese$MOVE) )+
  ylim(1520, 1530)+
  ylab("Total Sales")+
  ggtitle("Total Units Sold in Weeks 1, 199 and 399", subtitle = "Source: Dominick's Finer Food Database")+
  theme_economist()



#Plot 5 - count of SALE values of store x,y,z across #of weeks 
#BOX PLOT OF UPC X at Store Y

cheeseBox <- cheese
cheeseBox <-filter(cheeseBox, UPC == 1600063840)
table(cheeseBox$STORE)
#store 136 has most occurrences
cheeseBox <-filter(cheeseBox, STORE == 136)

Plot5<- ggplot(data=cheeseBox, aes(x = WEEK, y= MOVE ))+
geom_point(aes(size = PROFIT))+
  xlim(125,175)+
  ggtitle("Number of UPC 1600063840 Sold at Store 136 Between weeks 125 and 175", 
          subtitle = "Source: Dominick's Finer Food Database")+
  ylab("Units Sold")+
    theme_economist()


#plot 6 count of "L" "B" "S" "G" "C" across all weeks

plot6<- ggplot(data = cheeseSaleNulls)+
  geom_bar(mapping=aes(x=SALE))+
  xlab("Conditional Sale Type")+
  ylab("Count")+
  ggtitle("Total of Each Sale Condition Across All Weeks ", 
          subtitle = "Source: Dominick's Finer Food Database")+
  theme(panel.background = element_blank())+
  theme_economist()


#Generate Plots
plotA
plot1
plot3
plot4
plot6
Plot5
