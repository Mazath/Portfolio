
#install packages and import libraries
install.packages('readr')
library(readr)
install.packages('tidyverse')
library(tidyverse)
install.packages("rapport")
library(rapport)
library(cluster)
library(factoextra)
install.packages('vtable')
library(vtable)
install.packages("glue")
library(glue)

#read csv file
catFinal <- read.csv('FinalCat.csv')

# change age to numeric
catFinal$Age<- as.numeric(catFinal$Age)

#change gender to sex
colnames(catFinal)[6]<- "Sex"

#convert all Breed info to uppercase
catFinal$Breed <- toupper(catFinal$Breed)
catFinal$Breed <- str_trim(catFinal$Breed) #remove any whitespace

# convert lowercase letters to uppercase then factorise Sex column
catFinal$Sex[catFinal$Sex=='m']<- "M"
catFinal$Sex[catFinal$Sex=='f']<- "F"
catFinal$Sex <- as.factor(catFinal$Sex)

#convert zeros to Na's for Activity, health and sleepscore
catFinal$HealthIndex[catFinal$HealthIndex==0] <-NA
catFinal$SleepScore[catFinal$SleepScore==0] <-NA
catFinal$ActivityIndex[catFinal$ActivityIndex==0] <-NA


#count Na's total
na_count <-sapply(catFinal, function(y) sum(length(which(is.na(y)))))
na_count <- data.frame(na_count)
na_count

# remove Na's from Activity Ind, Health Ind and Sleep score
catFinal <-catFinal[!is.na(catFinal$HealthIndex),]
catFinal <-catFinal[!is.na(catFinal$ActivityIndex),]
catFinal <-catFinal[!is.na(catFinal$SleepScore),]


# get the numeric columns only
DescriptiveCols <- c(5,7,9:17)
NumericColsCatData <- catFinal[DescriptiveCols]

#show the summary statistics for the numeric columns
sumtable(NumericColsCatData, title='Summary Statistics for Cat Data')


# look at spread of data - outliers, of numeric columns

boxplot(NumericColsCatData, main="Boxplots showing outliers")


#Detect and remove outliers for analysis

for(i in 2:ncol(catFinal)){
   x <- catFinal
  if(is_numeric(catFinal[ ,i])){
     #detect outliers for the column
     outliers <- boxplot(catFinal[ ,i], plot=FALSE)$out
 
    if (is_empty(outliers)){#if no outliers return the data 
        catFinal <-x
    }
    else{
       #remove the outliers for that column and then return the data 
       x<- x[-which(x[ ,i] %in% outliers),]
       catFinal <- x
     }
  }
   else{# if non-numeric column skip to next iteration
     next
   }
}
# refill the descriptive columns with the data without outliers
NumericColsCatData <- catFinal[DescriptiveCols]


# look at new boxplot after  - outliers removed of numeric columns
boxplot(NumericColsCatData, main="Boxplots showing outliers")

#show the summary statistics for the numeric columns
sumtable(NumericColsCatData, title='Summary Statistics for Cat Data')

# find total # unique catsdata after removal of outliers
length(unique(catFinal$UniqueCatId))

#find # cats in single cat homes

singleCats <- subset(catFinal, NumOtherCat==0)
length(unique(singleCats$UniqueCatId))


## mini histogram plots
p1 <- ggplot(catFinal, aes(BarkPoints))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  ggtitle("Distribution of BarkPoints")+theme_bw()

p2 <- ggplot(catFinal, aes(HealthIndex))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  xlim(0.5,0.9)+
  ggtitle("Distribution of Health Index")+theme_bw()

p3 <- ggplot(catFinal, aes(Calories))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  ggtitle("Distribution of Calories")+theme_bw()

p4 <- ggplot(catFinal, aes(SleepScore))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  ggtitle("Distribution of Sleep Score")+theme_bw()

p5 <- ggplot(catFinal, aes(ActivityIndex))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  xlim(0,2)+
  ggtitle("Distribution of Activity Index")+theme_bw()

p6 <- ggplot(catFinal, aes(Play))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  ggtitle("Distribution of Play")+theme_bw()

p7 <- ggplot(catFinal, aes(Active))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  ggtitle("Distribution of Active")+theme_bw()

p8 <- ggplot(catFinal, aes(Rest))+
  geom_histogram(fill="lightsteelblue4",bins =40)+
  ggtitle("Distribution of Rest")+theme_bw()


gridExtra::grid.arrange(p1,p2,p3,p4,p5,p6,p7,p8,ncol=4, 
                        top="Distribution Histograms")

## solo cats V's multiple cats
# create subset and a column to factorise for each
solocats <- subset(catFinal,catFinal$NumOtherCat == 0)
solocats$Num <-0

multicats <- subset(catFinal, catFinal$NumOtherCat != 0)
multicats$Num <- 1

#Bind the two subsets into 1 dataframe
catNumbers <- rbind(cbind(data=1, solocats),
                    cbind(data=2, multicats))

#Boxplot showing comparison of health index 
ggplot(catNumbers, aes(x=Num, y=HealthIndex, fill=as.factor(Num)))+
  geom_boxplot()+
  labs(x="Number of other cats", y="Health Index")+
  guides(x="none")+
  ggtitle("Health Index comparison between single and multi cats")+
  scale_fill_manual( values=c('mediumseagreen', 'slateblue2'),
                     labels=c('Single Cat Homes', 'Multi Cat Homes'))+
  theme_bw()



#print unique cat breeds
print(unique(catFinal$Breed))


# long haired cat subset

longHairBreeds <- subset(catFinal, catFinal$Breed == "DOMESTIC LONG HAIR"|catFinal$Breed == "MAINE COON" |
                         catFinal$Breed == "RAGDOLL"|catFinal$Breed == "DOMESTIC MEDIUM HAIR"|
                         catFinal$Breed == "NORWEGIAN FOREST"|catFinal$Breed =="MAIN  COON"|
                         catFinal$Breed == "FORWEGIAN FOREST" |catFinal$Breed == "DOMESTIC MEDIUM HAOR"|
                         catFinal$Breed ==  "FLAME POINT RAGDOLL")
                                                     
                                                     
                                                     
 #Short hair cats
shortHairBreeds <- subset(catFinal, catFinal$Breed == "BURMESE"|catFinal$Breed == "ORIENTAL"  |
                          catFinal$Breed == "DOMESTIC SHORTHAIR"|catFinal$Breed ==  "TABBY"|
                          catFinal$Breed == "DOMESTIC SHORT HAIR"|catFinal$Breed =="SCOTTISH SHORTHAIR"|
                          catFinal$Breed == "SPHYNX" |catFinal$Breed == "RUSSIAN BLUE"|
                          catFinal$Breed ==  "BRITISH SHORTHAIR" |catFinal$Breed =="BRITISH SHORT HAIR"|
                          catFinal$Breed =="CANADIAN SPHYN" |catFinal$Breed =="CORNISH REX")


# subset by cat ages
catAges <- cbind.data.frame(catFinal$Age, catFinal$UniqueCatId)    

colnames(catAges)<-c("Age", "UniqueCatId")


