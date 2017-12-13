/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.compute.samples;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.network.Network;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import com.microsoft.rest.LogLevel;

import java.util.HashMap;

/**
 * Azure Compute sample for managing virtual machines -
 *   - Create a virtual machine with Managed Service Identity enabled with access to resource group
 *   - Set custom script in the virtual machine that
 *          - install az cli in the virtual machine
 *          - uses az cli MSI credentials to create a storage account
 *   - Get storage account created through MSI credentials.
 */
public final class ExplicitMSILogin {
    /**
     * Main entry point.
     * @param args the parameters
     */
    public static void main(String[] args) {
        try {

            String identityId = System.getenv("AZURE_IDENTITY_ID");
            if (identityId == null) {
                System.out.println("Expected identity not found, please set 'AZURE_IDENTITY_ID'");
                return;
            }
            System.out.println("ms_res_id: " + identityId);

            String subscriptionId = System.getenv("AZURE_SUBSCRIPTION_ID");
            if (subscriptionId == null) {
                System.out.println("Expected subscription not found, please set 'AZURE_SUBSCRIPTION_ID'");
                return;
            }

            final Region region = Region.fromName("Central US EUAP");

            final AzureEnvironment canarayEnv = new AzureEnvironment(new HashMap<String, String>() {
                {
                    this.put("portalUrl", "http://go.microsoft.com/fwlink/?LinkId=254433");
                    this.put("publishingProfileUrl", "http://go.microsoft.com/fwlink/?LinkId=254432");
                    this.put("managementEndpointUrl", "https://management.core.windows.net/");
                    this.put("resourceManagerEndpointUrl", "https://brazilus.management.azure.com/");
                    this.put("sqlManagementEndpointUrl", "https://management.core.windows.net:8443/");
                    this.put("sqlServerHostnameSuffix", ".database.windows.net");
                    this.put("galleryEndpointUrl", "https://gallery.azure.com/");
                    this.put("activeDirectoryEndpointUrl", "https://login.microsoftonline.com/");
                    this.put("activeDirectoryResourceId", "https://management.core.windows.net/");
                    this.put("activeDirectoryGraphResourceId", "https://graph.windows.net/");
                    this.put("dataLakeEndpointResourceId", "https://datalake.azure.net/");
                    this.put("activeDirectoryGraphApiVersion", "2013-04-05");
                    this.put("storageEndpointSuffix", ".core.windows.net");
                    this.put("keyVaultDnsSuffix", ".vault.azure.net");
                    this.put("azureDataLakeStoreFileSystemEndpointSuffix", "azuredatalakestore.net");
                    this.put("azureDataLakeAnalyticsCatalogAndJobEndpointSuffix", "azuredatalakeanalytics.net");
                }
            });

            final com.microsoft.azure.management.compute.samples.MSICredentials credentials = new com.microsoft.azure.management.compute.samples.MSICredentials(canarayEnv);

            credentials.withIdentityId(identityId);

            Azure azure = Azure.configure()
                    .withLogLevel(LogLevel.BODY_AND_HEADERS)
                    .authenticate(credentials)
                    .withSubscription(subscriptionId);

            System.out.println("Selected subscription: " + azure.subscriptionId());

            // Create a virtual network
            //
            Network network = azure.networks()
                    .define("nw123erty")
                    .withRegion(region)
                    .withExistingResourceGroup("37619e6defc446a")
                    .create();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private ExplicitMSILogin() {
    }
}