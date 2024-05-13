# Use the .NET 8 SDK image as a build environment
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /source

# Copy the project file and restore dependencies
COPY . .
RUN dotnet restore "./Tortee.API/Tortee.API.csproj" --disable-parallel
RUN dotnet publish "./Tortee.API/Tortee.API.csproj" -c release -o /app --disable-parallel --no-restore


# Use the ASP.NET Core runtime image for the final image
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS runtime
WORKDIR /app
COPY --from=build /app ./

# Expose port 8086 for HTTP traffic
EXPOSE 5000

# Set the entry point for the application
ENTRYPOINT ["dotnet", "Tortee.API.dll"]
