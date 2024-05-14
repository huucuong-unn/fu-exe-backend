FROM mcr.microsoft.com/dotnet/sdk:8.0 AS base
WORKDIR /app
EXPOSE 8080
EXPOSE 8081

# Use the .NET 8 SDK image as a build environment
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
ARG BUILD_CONFIGURATION=Debug
WORKDIR /src
COPY ["Tortee.API/Tortee.API.csproj", "Tortee.API/"]
COPY ["Tortee.Repository/Tortee.Repository.csproj", "Tortee.Repository/"]
COPY ["Tortee.BussinessObject/Tortee.BussinessObject.csproj", "Tortee.BussinessObject/"]
RUN dotnet restore "./Tortee.API/Tortee.API.csproj" 
COPY . .
WORKDIR "/src/Tortee.API"   
RUN dotnet build "Tortee.API.csproj" -c %BUILD_CONFIGURATION% -o /app/build


FROM build AS publish
ARG BUILD_CONFIGURATION=Debug
RUN dotnet publish "Tortee.API.csproj" -c %BUILD_CONFIGURATION% -o /app/publish /p:UseAppHost=false


FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Tortee.API.dll"]