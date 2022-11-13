import { Divider, Grid, Heading, Skeleton, useQuery, VStack } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import PostServices from '../services/post-services'
import { PostCard } from './PostCard'

export const SearchPage = () => {

    const [post, setPost] = useState()

    const query = useSearchParams()

    useEffect(() => {
        PostServices.getPostByTitle(query[0].get('title')).then(resp => setPost(resp))
    }, [])


    return (
        <>
            <VStack justify={"center"} mb={10}>
                <Heading size={"lg"} textAlign={"center"}>View all blog posts</Heading>
                <Divider width={"50%"} />
            </VStack>
            <Grid ml={10} mr={10} gap={20} templateColumns='repeat(auto-fill, minmax(25%, 1fr))'>
                {post ? post.map(item => {
                    return <PostCard key={item.post_id} content={item} />
                })
                    : <Skeleton />
                }
            </Grid>
        </>
    )
}
